package com.moviegetter.ui.download

import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity

/**
 * Created by lizhidan on 2019-06-06.
 */
class DownloadActivity : MGBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
    }

    override fun getPresenter(): BasePresenter<*>? = null
}