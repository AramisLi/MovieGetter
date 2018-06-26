package com.moviegetter.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.RelativeLayout
import com.aramis.library.base.BaseActivity
import com.moviegetter.R
import org.jetbrains.anko.backgroundResource

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
abstract class MGBaseActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val titleView = findViewById<Toolbar>(R.id.toolbar)
        titleView?.backgroundResource = R.color.colorPrimary
    }
}