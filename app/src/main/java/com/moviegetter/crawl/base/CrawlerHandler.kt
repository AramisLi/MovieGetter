package com.moviegetter.crawl.base

import android.os.Handler

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
open class CrawlerHandler {

    fun create(callback: ((what: Int, obj: Any?) -> Unit)?): Handler {
        return Handler(Handler.Callback {
            callback?.invoke(it.what, it.obj)
            false
        })
    }

    fun createWithListener(start: ((what: Int, obj: Any?) -> Unit)? = null,
                           htmlSuccess: ((what: Int, obj: Any?) -> Unit)? = null,
                           htmlFail: ((what: Int, obj: Any?) -> Unit)? = null,
                           parseSuccess: ((what: Int, obj: Any?) -> Unit)? = null,
                           parseFail: ((what: Int, obj: Any?) -> Unit)? = null,
                           dbSuccess: ((what: Int, obj: Any?) -> Unit)? = null,
                           dbFail: ((what: Int, obj: Any?) -> Unit)? = null,
                           skip: ((what: Int, obj: Any?) -> Unit)? = null,
                           finished: ((what: Int, obj: Any?) -> Unit)? = null,
                           finally: ((what: Int, obj: Any?) -> Unit)? = null): Handler {
        return Handler(Handler.Callback {
            when (it.what) {

                CrawlerHandlerWhat.CRAWLER_START -> {
                    start?.invoke(it.what, it.obj)
                }
                CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS -> {
                    htmlSuccess?.invoke(it.what, it.obj)
                }
                CrawlerHandlerWhat.CRAWLER_HTML_FAIL -> {
                    htmlFail?.invoke(it.what, it.obj)
                }
                CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS -> {
                    parseSuccess?.invoke(it.what, it.obj)
                }
                CrawlerHandlerWhat.CRAWLER_PARSER_FAIL -> {
                    parseFail?.invoke(it.what, it.obj)
                }
                CrawlerHandlerWhat.CRAWLER_DB_SUCCESS -> {
                    dbSuccess?.invoke(it.what, it.obj)
                }
                CrawlerHandlerWhat.CRAWLER_DB_FAIL -> {
                    dbFail?.invoke(it.what, it.obj)
                }
                CrawlerHandlerWhat.CRAWLER_SKIP -> {
                    skip?.invoke(it.what, it.obj)
                }
                CrawlerHandlerWhat.CRAWLER_FINISHED -> {
                    finished?.invoke(it.what, it.obj)
                }

            }
            finally?.invoke(it.what, it.obj)
            false
        })
    }

    fun createByHint(l: ((what: Int, hint: String, obj: Any?) -> Unit)? = null,
                     finally: ((what: Int, obj: Any?) -> Unit)? = null): Handler {
        return createWithListener(
                start = { what, obj -> l?.invoke(what, "开始爬取", obj) },
                htmlSuccess = { what, obj -> l?.invoke(what, "html获取成功", obj) },
                htmlFail = { what, obj -> l?.invoke(what, "html获取失败", obj) },
                parseSuccess = { what, obj -> l?.invoke(what, "解析成功", obj) },
                parseFail = { what, obj -> l?.invoke(what, "解析失败", obj) },
                dbSuccess = { what, obj -> l?.invoke(what, "数据库保存成功", obj) },
                dbFail = { what, obj -> l?.invoke(what, "数据库保存失败", obj) },
                skip = { what, obj -> l?.invoke(what, "跳过", obj) },
                finished = { what, obj -> l?.invoke(what, "爬取完成", obj) },
                finally = { what, obj -> finally?.invoke(what, obj) }
        )
    }

    private var total = 0
    private var update = 0
    private var fail = 0
    private var finished = false
    fun createByCount(l: ((total: Int, update: Int, fail: Int, finished: Boolean) -> Unit)? = null): Handler {
//        handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean)
        return createWithListener(
                start = { what, obj -> total++ },
                htmlFail = { what, obj -> fail++ },
                parseFail = { what, obj -> fail++ },
                dbFail = { what, obj -> fail++ },
                dbSuccess = { what, obj -> update++ },
                skip = { what, obj -> },
                finished = { what, obj -> finished = true },
                finally = { what, obj ->
                    l?.invoke(total, update, fail, finished)
                    if (finished) {
                        total = 0
                        update = 0
                        fail = 0
                        finished = false
                    }
                })
    }

}