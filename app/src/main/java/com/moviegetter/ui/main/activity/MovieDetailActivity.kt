package com.moviegetter.ui.main.activity

import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.ui.main.adapter.MainSimpleAdapter
import kotlinx.android.synthetic.main.activity_dytt_detail.*
import java.nio.charset.Charset
import android.widget.Toast
import android.content.Intent
import android.net.Uri


/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class MovieDetailActivity : MGBaseActivity() {
    private var data: DYTTItem? = null
    private val urlList = mutableListOf<String>()
    private var adapter = MainSimpleAdapter(urlList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dytt_detail)
        getDataFromIntent()
        initView()
        setListener()
        initData()
    }

    private fun setListener() {
        list_detail_urls.setOnItemClickListener { parent, view, position, id ->
            openXf()
        }
    }

    private fun initData() {
        data?.apply {
            if (this.richText != null) {
                logE(this.richText)
                logE("==============" + getEncoding(richText!!))
                logE(Charset.defaultCharset().toString())
//                val a = "<html><head><title> 欢迎您 </title></head>" +
//                        "<body>" + richText + "</body></html>"
//                web_detail.loadData(String(richText!!.toByteArray(Charset.forName("GB2312")),Charset.forName("UTF-8")), "text/html", "UTF-8")
                web_detail.settings.defaultTextEncodingName = "UTF-8"
//                web_detail.loadData(String(richText!!.toByteArray(Charset.forName("ISO-8859-1")),Charset.forName("GBK")), "text/html", "GBK")

                web_detail.loadData(richText, "text/html", "UTF-8")

//                web_detail.loadUrl("http://www.dytt8.net/html/gndy/dyzz/20180620/57020.html")
            }
            if (downloadName != null) {
                if (downloadName!!.contains(",")) {
                    urlList.addAll(downloadName!!.split(","))
                } else {
                    urlList.add(downloadName!!)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun getEncoding(str: String): String {
        val encode = arrayOf("UTF-8", "ISO-8859-1", "GB2312", "GBK", "GB18030", "Big5", "Unicode", "ASCII")
        for (i in encode.indices) {
            try {
                if (str == String(str.toByteArray(charset(encode[i])), Charset.forName(encode[i]))) {
                    return encode[i]
                }
            } catch (ex: Exception) {
            }

        }

        return ""
    }

    fun openXunlei() {
        try {
            val link = "thunder://QUFodHRwOi8vZGwxNTEuODBzLmltOjkyMC8xNzExL+i/veW/hi/ov73lv4YubXA0Wlo="
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            intent.addCategory("android.intent.category.DEFAULT")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show()
        }

    }

    fun openXf() {
        val appPackageName = "com.xfplay.play"

        try {
//            val intent = this.packageManager.getLaunchIntentForPackage(appPackageName)
            val link = "xfplay://QUFodHRwOi8vZGwxNTEuODBzLmltOjkyMC8xNzExL+i/veW/hi/ov73lv4YubXA0Wlo="
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            intent.putExtra("link",link)
            intent.putExtra("data",link)
            intent.addCategory("android.intent.category.DEFAULT")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show()
        }

    }

    private fun initView() {
        list_detail_urls.adapter = adapter
    }

    private fun getDataFromIntent() {
        data = intent.getSerializableExtra("data") as DYTTItem
    }

    override fun getPresenter(): BasePresenter<*>? = null
}