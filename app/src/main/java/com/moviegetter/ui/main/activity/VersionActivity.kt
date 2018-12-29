package com.moviegetter.ui.main.activity

import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:
 */
class VersionActivity: MGBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_version)
    }
    override fun getPresenter(): BasePresenter<*>?=null
}