package com.moviegetter.aac

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.aramis.library.R
import com.aramis.library.extentions.logE
import com.moviegetter.service.ITaskManager
import com.moviegetter.service.SpiderService
import org.jetbrains.anko.displayMetrics

/**
 * Created by lizhidan on 2019/1/21.
 */
open class AACActivity : AppCompatActivity() {
    protected var screenWidth: Int = 0
    protected var screenHeight: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initControl()
    }

    private fun initControl() {
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
    }

    //设置toolbar右边的textView
    protected fun setTitleRightText(text: String?, onClickListener: View.OnClickListener?) {
        val text_toolbar_right = findViewById<View>(R.id.text_toolbar_right)
        if (text_toolbar_right != null) {
            (text_toolbar_right as TextView).visibility = View.VISIBLE
            if (!TextUtils.isEmpty(text)) text_toolbar_right.text = text
            text_toolbar_right.setOnClickListener(onClickListener)
        }else{
            logE("title right textView is null")
        }
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


    fun bindCrawlService(connectedSuccess: (ITaskManager) -> Unit,
                         connectedFail: ((errorMes: String) -> Unit)? = null,
                         disconnectedFail: ((name: ComponentName?) -> Unit)? = null) {
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val iTaskManager = ITaskManager.Stub.asInterface(service)
                if (iTaskManager != null) {
                    connectedSuccess.invoke(iTaskManager)
                    // http://www.zhiboo.net/
//                    iTaskManager?.registerListener(presenter.iOnNewNodeGetListener)

                } else {
                    connectedFail?.invoke("链接服务失败:null")
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                disconnectedFail?.invoke(name)
            }

        }
        bindService(Intent(this, SpiderService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

}