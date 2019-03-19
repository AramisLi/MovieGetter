package com.moviegetter.ui.main.activity

import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.ui.main.pv.VersionActivityDescription
import kotlinx.android.synthetic.main.activity_version.*

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:版本信息
 */
class VersionActivity : MGBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_version)

        text_description.text = VersionActivityDescription.description()

    }

    override fun getPresenter(): BasePresenter<*>? = null
}