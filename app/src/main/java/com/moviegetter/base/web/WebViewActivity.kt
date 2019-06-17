package com.moviegetter.base.web

import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.*
import com.aramis.library.extentions.logE

/**
 * Created by lizhidan on 2019-06-06.
 */
class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private var loadUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)
        getDataFromIntent()
        initView()
    }

    private fun getDataFromIntent() {
        loadUrl = intent.getStringExtra("loadUrl")
    }

    private fun initView() {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.allowFileAccess = true
        settings.javaScriptCanOpenWindowsAutomatically = true

        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.databaseEnabled = true
        settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                logE("shouldOverrideUrlLoading url:$url")
                webView.loadUrl(url)

                return true
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//                super.onReceivedSslError(view, handler, error)
                handler?.proceed()
            }

            //获取资源时调用此方法(图片等)
//            override fun onLoadResource(view: WebView?, url: String?) {
//                logE("onLoadResource url:$url")
//                super.onLoadResource(view, url)
//            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        if (loadUrl.isNotBlank()) {
            webView.loadUrl(loadUrl)
//            webView.loadUrl("", mapOf())
        }
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}