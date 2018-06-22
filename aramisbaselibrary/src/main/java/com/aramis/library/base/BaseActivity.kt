package com.aramis.library.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.aramis.library.R
import com.aramis.library.aramis.WebBrowserActivity
import com.aramis.library.component.dialog.BeforeLoadingDialog
import com.aramis.library.ui.dialog.LoadingDialog
import rx.Subscription


/**
 * BaseActivity
 * Created by Aramis on 2017/4/26.
 */

abstract class BaseActivity : AppCompatActivity(), BaseView {
    val TAG = "===" + javaClass.simpleName + "==="
    protected var screenWidth: Int = 0
    protected var screenHeight: Int = 0

    protected lateinit var exeloadingDialog: LoadingDialog
    private var isNeedLoadingDialog = true
    private var isNeedAutoBackView = true

    private val bundleSubscribe: Subscription? = null
    protected var useDefaultTitleColor = true

    protected val titleMiddleTextView: TextView
        get() = findViewById<View>(R.id.text_toolbar_middle) as TextView

    private val presenter: BasePresenter<*>? = null
    abstract fun getPresenter(): BasePresenter<*>?

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        StatusBarUtil.transparencyBar(this)
//        Eyes.translucentStatusBar(this, true)
//        if (useDefaultTitleColor) {
////            ScreenUtils.setDefaultTitleColor(this)
////            ScreenUtils.setActionBarTransColor(this, ContextCompat.getColor(this, R.color.bg_toolbar))
//            ScreenUtils.setActionBarTransColor(this, 0xffffffff.toInt())
//        }
//        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar))
        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels
        exeloadingDialog = BeforeLoadingDialog(this)

    }

    protected fun toSystemContacts() {
        //        Uri uri = Uri.parse("content://contacts/people");
        //        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, RequestCode_Contacts)
    }

    override fun onResume() {
        super.onResume()
        tryToAddBackClick()
    }

    //默认返回键
    private fun tryToAddBackClick() {
        val backView = findViewById<View>(R.id.image_toolbar_back)
        if (isNeedAutoBackView && backView != null) {
            backView.setOnClickListener { onBackPressed() }
        }
    }

    //设置是否需要默认返回
    fun setNeedAutoBackView(isNeedAutoBackView: Boolean) {
        this.isNeedAutoBackView = isNeedAutoBackView
    }

    //设置toolbar右边的textView
    protected fun setTitleRightText(text: String?, onClickListener: View.OnClickListener) {
        val text_toolbar_right = findViewById<View>(R.id.text_toolbar_right)
        if (text_toolbar_right != null) {
            (text_toolbar_right as TextView).visibility = View.VISIBLE
            if (!TextUtils.isEmpty(text)) text_toolbar_right.text = text
            text_toolbar_right.setOnClickListener(onClickListener)
        }
    }

    fun setTitleRightText(onClickListener: View.OnClickListener) {
        setTitleRightText(null, onClickListener)
    }

    //设置toolbar中间的textView
    protected fun setTitleMiddleText(text: String) {
        setTitleMiddleText(View.VISIBLE, text)
    }

    //设置toolbar中间的textView
    protected fun setTitleMiddleText(visibility: Int, text: String) {
        val text_toolbar_middle = findViewById<View>(R.id.text_toolbar_middle)
        if (text_toolbar_middle != null) {
            (text_toolbar_middle as TextView).visibility = visibility
            text_toolbar_middle.text = text
        }
    }

    protected fun getBaseToolbar(): Toolbar? {
        return findViewById(R.id.toolbar)
    }

    //设置title line显示
    protected fun setTitleLineVisible(visible: Int) {
        val titleView = findViewById<View>(R.id.line_toolbar)
        if (titleView != null) {
            titleView.visibility = visible
        }
    }

    protected fun setTitleLeftNavigator(onClickListener: View.OnClickListener) {
        setTitleLeftNavigator(-1, onClickListener)
    }

    protected fun setTitleLeftNavigator(resId: Int, onClickListener: View.OnClickListener) {
        setTitleLeftNavigator(View.VISIBLE, resId, onClickListener)
    }

    protected fun setTitleLeftNavigator(visibility: Int, resId: Int, onClickListener: View.OnClickListener) {
        var image_toolbar_left = findViewById<View>(R.id.image_toolbar_left)
        val imageBack = findViewById<View>(R.id.image_toolbar_back)
        if (imageBack != null) {
            imageBack.visibility = View.GONE
        }
        if (image_toolbar_left != null) {
            image_toolbar_left = image_toolbar_left as ImageView
            if (visibility == View.VISIBLE) {
                if (resId > 0) image_toolbar_left.setImageResource(resId)
                image_toolbar_left.setOnClickListener(onClickListener)
            } else {
                image_toolbar_left.visibility = visibility
            }
        }
    }

    protected fun setTitleRightNavigator(resId: Int = -1, onClickListener: View.OnClickListener) {
        val image_toolbar_right = findViewById<View>(R.id.image_toolbar_right)
        if (image_toolbar_right != null) {
            (image_toolbar_right as ImageView).visibility = View.VISIBLE
            if (resId != -1) {
                image_toolbar_right.setImageResource(resId)
            }
            image_toolbar_right.setOnClickListener(onClickListener)
        }
    }

    protected fun getTitleRightImageView(): ImageView? = findViewById(R.id.image_toolbar_right)

    //隐藏键盘
    protected fun hideKeyboard() {
        (getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    protected fun toOtherActivity(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clazz)
        if (bundle != null) intent.putExtras(bundle)
        startActivity(intent)
    }

    //toWebActivity
    protected fun toWebActivity(webUrl: String, title: String) {
        val intent = Intent(this, WebBrowserActivity::class.java)
        intent.putExtra(WebBrowserActivity.WEB_URL, webUrl)
        intent.putExtra(WebBrowserActivity.WEB_TITLE, title)
        startActivity(intent)
    }

    protected fun needLoadingDialog(isNeed: Boolean) {
        this.isNeedLoadingDialog = isNeed
    }

    override fun getLoadingDialog(): LoadingDialog {
        return exeloadingDialog
    }

    override fun onNetError(errorCode: Int, errorMessage: String) {
        var _errorMessage = errorMessage
        if (errorCode == -1) {
            _errorMessage = "无网络，请打开网络或者移动数据"
        } else if (TextUtils.isEmpty(errorMessage)) {
            _errorMessage = "网络错误：$errorCode"
        }
        Toast.makeText(this, _errorMessage, Toast.LENGTH_SHORT).show()
    }

    fun onTokenUpdate() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.dispatchView()
        bundleSubscribe?.unsubscribe()
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("", true)
        setResult(0, intent)
        super.onBackPressed()
    }

    companion object {
        protected val RequestCode_Contacts = 32
    }
}
