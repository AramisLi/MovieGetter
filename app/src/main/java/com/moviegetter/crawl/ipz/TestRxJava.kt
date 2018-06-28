package com.moviegetter.crawl.ipz

import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.Serializable

/**
 *Created by Aramis
 *Date:2018/6/28
 *Description:
 */
class TestRxJava {
    fun start(event: (TestRxJavaEvent) -> Unit) {
        Thread(Runnable {
            var i = 0
            while (i < 10) {
                val cc = TestRxJavaEvent("aramis", 12, "我是大帅哥", i)
                Observable.just(cc)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            event.invoke(it)
                        }
                Thread.sleep(1000)
                i++
            }
        }).start()

    }

    fun start2(sub: Subscriber<TestRxJavaEvent>) {
        Thread(Runnable {
            var i = 0
            while (i < 10) {
                val cc = TestRxJavaEvent("aramis", 12, "我是大帅哥", i)
//                Observable.just(cc)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(sub)

                Observable.create<TestRxJavaEvent> {
                    it.onNext(cc)
                }.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(sub)

                Thread.sleep(1000)
                logE("i'm running$i")
                i++
            }
        }).start()

    }

    fun start3() {
        Thread(Runnable {
            var i = 0
            while (i < 10) {
                val cc = TestRxJavaEvent("aramis", 12, "我是大帅哥", i)
//                ArBus.getDefault().bus().observeOn(AndroidSchedulers.mainThread())
//                        .subscribe()

                ArBus.getDefault().post(cc)
                Thread.sleep(1000)
                logE("i'm running$i")
                i++
            }
        }).start()

    }


}

data class TestRxJavaEvent(val name: String, val age: Int, val test: String, val index: Int) : Serializable