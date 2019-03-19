package com.moviegetter.crawl.base

import android.content.Context

class ArCrawler private constructor() {
    companion object {
        class Builder(url: String, parser: Parser) {
            private val arCrawler = ArCrawler()

            init {
                arCrawler.url = url
                arCrawler.parser = parser
            }

            fun onCrawlStart(onCrawlStart: (() -> Unit)?) {
                arCrawler.onCrawlStart = onCrawlStart
            }

            fun onCrawlFinish(onCrawlFinish: (() -> Unit)?) {
                arCrawler.onCrawlFinish = onCrawlFinish
            }

            fun onCrawlNewArrive(onCrawlNewArrive: (() -> Unit)?) {
                arCrawler.onCrawlNewArrive = onCrawlNewArrive
            }


            fun build(): ArCrawler {
                return arCrawler
            }
        }
    }

    private var url = ""
    private var parser: Parser? = null
    private var onCrawlStart: (() -> Unit)? = null
    private var onCrawlFinish: (() -> Unit)? = null
    private var onCrawlNewArrive: (() -> Unit)? = null

    fun start(context: Context, useProcess: Boolean = false) {
        if (useProcess) {
            //启动service
        } else {

        }
    }

}