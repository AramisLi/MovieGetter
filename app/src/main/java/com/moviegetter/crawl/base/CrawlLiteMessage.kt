package com.moviegetter.crawl.base

import com.aramis.library.aramis.ArBus
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.io.Serializable

data class CrawlLiteMessage(val what: Int, val obj: Any?, val tag: String? = null, val position: Int = 0) : Serializable

class CrawlLiteSubscription {

    fun getCrawlSubscription(start: ((what: Int, obj: Any?) -> Unit)? = null,
                             htmlSuccess: ((what: Int, obj: Any?) -> Unit)? = null,
                             htmlFail: ((what: Int, obj: Any?) -> Unit)? = null,
                             parseSuccess: ((what: Int, obj: Any?) -> Unit)? = null,
                             parseFail: ((what: Int, obj: Any?) -> Unit)? = null,
                             dbSuccess: ((what: Int, obj: Any?) -> Unit)? = null,
                             dbFail: ((what: Int, obj: Any?) -> Unit)? = null,
                             skip: ((what: Int, obj: Any?) -> Unit)? = null,
                             finished: ((what: Int, obj: Any?) -> Unit)? = null,
                             finally: ((what: Int, obj: Any?) -> Unit)? = null,
                             filter: ((CrawlLiteMessage) -> Boolean)? = null): Subscription {
        return ArBus.getDefault().take(CrawlLiteMessage::class.java)
                .filter {
                    filter != null && filter.invoke(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
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
                }
    }

    private var total = 0
    private var update = 0
    private var fail = 0
    private var finished = false
    fun getCrawlCountSubscription(filter: ((CrawlLiteMessage) -> Boolean)? = null, l: ((total: Int, update: Int, fail: Int, finished: Boolean) -> Unit)? = null): Subscription {
        return getCrawlSubscription(
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
                }, filter = filter)
    }
}