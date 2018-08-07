package com.moviegetter.crawl.ipz

import android.content.Context
import com.aramis.library.extentions.getTimestamp
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.BasePipeline
import com.moviegetter.crawl.base.Item
import com.moviegetter.utils.database
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import kotlin.math.log

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class IPZPipeline : BasePipeline() {
    override fun pipeHook(context: Context?, items: List<Item>) {
        logE("保存item:$items")
        context?.database?.use {
            items.filter { it is IPZItem }.map { it as IPZItem }.forEach {
                if (select(DBConfig.TABLE_NAME_ADY).whereArgs("movieId = \"${it.movieId}\"").exec { this.count } > 0) {
                    logE("update update update")
                    val dbItem = select(DBConfig.TABLE_NAME_ADY).whereArgs("movieId = {movieId}", "movieId" to it.movieId).parseList(object : RowParser<IPZItem> {
                        override fun parseRow(columns: Array<Any?>): IPZItem {
                            return IPZItem((columns[0] as Long).toInt(), columns[1] as String, columns[2] as String?, columns[3] as String?)
                        }
                    })[0]

                    it.xf_url = formatXfurl(it.xf_url)

                    val save_xf_url = if (dbItem.xf_url != null) {
                        if (dbItem.xf_url!!.contains(",")) {
                            var b = false
                            for (i in dbItem.xf_url!!.split(",")) {
                                if (i == it.xf_url) {
                                    b = true
                                    break
                                }
                            }
                            if (b) {
                                dbItem.xf_url
                            } else {
                                dbItem.xf_url + "," + it.xf_url
                            }
                        } else {
                            if (dbItem.xf_url == it.xf_url) {
                                it.xf_url
                            } else {
                                dbItem.xf_url + "," + it.xf_url
                            }
                        }
                    } else {
                        it.xf_url
                    }
                    update(DBConfig.TABLE_NAME_DYTT,
                            "movieName" to it.movieName,
                            "movie_update_time" to it.movie_update_time,
                            "xf_url" to save_xf_url,
                            "update_time" to now(),
                            "movie_update_timestamp" to (it.movie_update_time?.getTimestamp("yyyy-MM-dd")
                                    ?: 0)).whereArgs("movieId = {movieId}", "movieId" to it.movieId).exec()
                } else {
                    logE("insert insert insert")
                    insert(DBConfig.TABLE_NAME_ADY,
                            "movieId" to it.movieId,
                            "movieName" to it.movieName,
                            "movie_update_time" to it.movie_update_time,
                            "xf_url" to it.xf_url,
                            "update_time" to now(),
                            "create_time" to now(),
                            "movie_update_timestamp" to (it.movie_update_time?.getTimestamp("yyyy-MM-dd")
                                    ?: 0),
                            "thumb" to it.thumb,
                            "images" to it.images,
                            "position" to it.position)
                }
            }
        }

    }

    private fun formatXfurl(playData: String?): String? {
        return if (playData != null && playData.contains("xfplay://")) {
            logE("ininin $playData")
            playData.substring(playData.indexOf("xfplay://")  until playData.length)
        } else {
            playData
        }
    }

//    IPZItem(movieId=40117, movieName=18, movie_update_time=2018-06-19,
//    xf_url=xfplay://dna=mwi2AHffmwH0DwEbAHEcExfcA0fgAZLWDwAbAda0AZDYBGAdmeIeAa|dx=673410790|mz=\u66F4\u591A\u5185\u5BB9www.ady69.com@fc2ppv_855043cd1.rmvb|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZL6BGa4mc94MzXPozS|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZp6BGa4mc94MzXPozS$xfplay',
//    // '\u7B2C02\u96C6$xfplay://dna=EGaZDwqcBee5BdLZEdfemGa3EeL2mde3BemZmdxWDwAdAHHXBHiYmt|dx=693985052|mz=\u66F4\u591A\u5185\u5BB9www.ady69.com@fc2ppv_855043cd2.rmvb|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZL6BGa4mc94MzXPozS|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZp6BGa4mc94MzXPozS, update_time=2018-08-07 16:09:53, create_time=2018-08-07 16:09:53, movie_update_timestamp=1529337600000, thumb=http://www.xfa90.com/pic/uploadimg/2018-6/201861920431188464.jpg, images=,, ,, ,, position=3, downloaded=1, downloaded_time=2018-08-07 16:32:31)

//    IPZItem(movieId=40872, movieName=无问题, movie_update_time=2018-07-14,
//    xf_url=xfplay://dna=EdL0Awp0AGDWDZpWAGEdD0i5Adi3BeDXmde0AwH0DGH2BeLWmeIdma|dx=362565945|mz=\u66F4\u591A\u5185\u5BB9www.ady69.com@fc2ppv_876621cd1.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R$xfplay',
//    // '\u7B2C02\u96C6$xfplay://dna=DZqgmZbbmwyfDGLWAwm4mZbdAwqeAxi3DZL4mGHYDGyfDHH5DZueAt|dx=348295948|mz=\u66F4\u591A\u5185\u5BB9www.ady69.com@fc2ppv_876621cd2.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R, update_time=2018-08-07 16:35:41, create_time=2018-08-07 16:35:41, movie_update_timestamp=1531497600000, thumb=http://www.xfa90.com/pic/uploadimg/2018-7/20187146481758306.jpg, images=,, ,, ,, position=3, downloaded=1, downloaded_time=2018-08-07 16:38:21)

//    IPZItem(movieId=40871, movieName=第一次, movie_update_time=2018-07-14, xf_url=xfplay://dna=mGL1mHAemxfeDHi1mwp5m0MdEwHZEeffEwmXmZtZDwD1Eee5DHDZEa|dx=409492604|mz=\u66F4\u591A\u5185\u5BB9www.ady69.com@fc2ppv_876144.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R, update_time=2018-08-07 16:35:41, create_time=2018-08-07 16:35:41, movie_update_timestamp=1531497600000, thumb=http://www.xfa90.com/pic/uploadimg/2018-7/2018714643621124.jpg, images=,, ,, position=3, downloaded=1, downloaded_time=2018-08-07 16:44:01)

    //1.添加ipz详情
    //2.修复同一部影片的第二部分无法下载的bug
    //3.增加同一数据源全部同步1页功能
    //4.增加隐藏新世界功能
//    IPZItem(movieId=40872, movieName=无问题的传说级, movie_update_time=2018-07-14,
//    xf_url=xfplay://dna=EdL0Awp0AGDWDZpWAGEdD0i5Adi3BeDXmde0AwH0DGH2BeLWmeIdma|dx=362565945|mz=\u66F4\u591A\u5185\u5BB9www.ady69.com@fc2ppv_876621cd1.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R$xfplay',
    // '\u7B2C02\u96C6$xfplay://dna=DZqgmZbbmwyfDGLWAwm4mZbdAwqeAxi3DZL4mGHYDGyfDHH5DZueAt|dx=348295948|mz=\u66F4\u591A\u5185\u5BB9www.ady69.com@fc2ppv_876621cd2.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R, update_time=2018-08-07 16:51:49, create_time=2018-08-07 16:51:49, movie_update_timestamp=1531497600000, thumb=http://www.xfa90.com/pic/uploadimg/2018-7/20187146481758306.jpg, images=,, ,, ,, position=3, downloaded=1, downloaded_time=2018-08-07 16:55:33)

}