package com.moviegetter.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.ui.main.activity.SettingActivity
import com.moviegetter.ui.main.activity.VersionActivity
import com.moviegetter.ui.main.adapter.UserFragmentAdapter
import kotlinx.android.synthetic.main.frg_main_user.view.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:用户中心
 */
class UserFragment : MGBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main_user, container, false)
        initView()
        setListener()
        return mRootView
    }

    private fun setListener() {
        mRootView.list_user.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                3 -> startActivity<VersionActivity>()
                4 -> startActivity<SettingActivity>()
                else -> {
                    toast("敬请期待")
                }
            }
        }
    }

    private fun initView() {
        val list = mutableListOf("下载过的电影", "喜欢的电视", "喜欢的视频", "版本信息", "设置")
        mRootView.list_user.adapter = UserFragmentAdapter(list, 3)
    }
}