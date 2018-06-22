package com.aramis.library.widget

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
//import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.AbsListView
import android.widget.ListView
import com.aramis.library.R


/**
 * SwipeRefreshView
 * Created by lizhidan on 2018/3/19.
 */

class SwipeRefreshView(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {
    private val mScaledTouchSlop: Int
    private val mFooterView: View
    private var mListView: ListView? = null
    private var mListener: OnLoadMoreListener? = null

    /**
     * 正在加载状态
     */
    private var isLoading: Boolean = false
    private var mRecyclerView: RecyclerView? = null
    private var mItemCount: Int = 0

    fun isLoading(): Boolean {
        return isLoading
    }

    /**
     * 在分发事件的时候处理子控件的触摸事件
     */
    private var mDownY: Float = 0.toFloat()
    private var mUpY: Float = 0.toFloat()

    init {
        // 填充底部加载布局
        mFooterView = View.inflate(context, R.layout.view_footer, null)

        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // 获取ListView,设置ListView的布局位置
        if (mListView == null || mRecyclerView == null) {
            // 判断容器有多少个孩子
            if (childCount > 0) {
                // 判断第一个孩子是不是ListView
                if (getChildAt(0) is ListView) {
                    // 创建ListView对象
                    mListView = getChildAt(0) as ListView

                    // 设置ListView的滑动监听
                    setListViewOnScroll()
                } else if (getChildAt(0) is RecyclerView) {
                    // 创建ListView对象
                    mRecyclerView = getChildAt(0) as RecyclerView

                    // 设置RecyclerView的滑动监听
                    setRecyclerViewOnScroll()
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        when (ev.action) {
            MotionEvent.ACTION_DOWN ->
                // 移动的起点
                mDownY = ev.y
            MotionEvent.ACTION_MOVE ->
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData()
                }
            MotionEvent.ACTION_UP ->
                // 移动的终点
                mUpY = y
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 判断是否满足加载更多条件
     */
    private fun canLoadMore(): Boolean {
        // 1. 是上拉状态
        val condition1 = mDownY - mUpY >= mScaledTouchSlop
        if (condition1) {
//            Log.d(TAG, "------->  是上拉状态")
        }

        // 2. 当前页面可见的item是最后一个条目,一般最后一个条目位置需要大于第一页的数据长度
        var condition2 = false
        if (mListView != null && mListView!!.getAdapter() != null) {

            if (mItemCount > 0) {
                if (mListView!!.getAdapter().getCount() < mItemCount) {
                    // 第一页未满，禁止下拉
                    condition2 = false
                } else {
                    condition2 = mListView!!.getLastVisiblePosition() == mListView!!.getAdapter().getCount() - 1
                }
            } else {
                // 未设置数据长度，则默认第一页数据不满时也可以上拉
                condition2 = mListView!!.getLastVisiblePosition() == mListView!!.getAdapter().getCount() - 1
            }

        }

        if (condition2) {
//            Log.d(TAG, "------->  是最后一个条目")
        }
        // 3. 正在加载状态
        val condition3 = !isLoading
        if (condition3) {
//            Log.d(TAG, "------->  不是正在加载状态")
        }
        return condition1 && condition2 && condition3
    }

    fun setItemCount(itemCount: Int) {
        this.mItemCount = itemCount
    }

    /**
     * 处理加载数据的逻辑
     */
    private fun loadData() {
        println("加载数据...")
        if (mListener != null) {
            // 设置加载状态，让布局显示出来
            setLoading(true)
            mListener?.onLoadMore()
        }

    }

    /**
     * 设置加载状态，是否加载传入boolean值进行判断
     *
     * @param loading
     */
    fun setLoading(loading: Boolean) {

        // 修改当前的状态
        isLoading = loading
        if (isLoading) {
            if (android.os.Build.BRAND != "samsung") {
                // 显示布局
//            mListView!!.addFooterView(mFooterView)
            }
        } else {
            if (android.os.Build.BRAND != "samsung") {
                // 隐藏布局
//            mListView!!.removeFooterView(mFooterView)
            }

            // 重置滑动的坐标
            mDownY = 0f
            mUpY = 0f
        }
    }


    /**
     * 设置ListView的滑动监听
     */
    private fun setListViewOnScroll() {

        mListView!!.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData()
                }
            }

            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

            }
        })
    }


    /**
     * 设置RecyclerView的滑动监听
     */
    private fun setRecyclerViewOnScroll() {
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }


    /**
     * 上拉加载的接口回调
     */

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        this.mListener = listener
    }


    companion object {

        private val TAG = SwipeRefreshView::class.java.simpleName
    }
}