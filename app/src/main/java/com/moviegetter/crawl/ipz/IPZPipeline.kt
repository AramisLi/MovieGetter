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

                    val save_xf_url=if (dbItem.xf_url!=null){
                        if (dbItem.xf_url!!.contains(",")){
                            var b=false
                            for ( i in dbItem.xf_url!!.split(",")){
                                if (i==it.xf_url){
                                    b=true
                                    break
                                }
                            }
                            if (b){
                                dbItem.xf_url
                            }else{
                                dbItem.xf_url+","+ it.xf_url
                            }
                        }else{
                            if ( dbItem.xf_url==it.xf_url){
                                it.xf_url
                            }else{
                                dbItem.xf_url+","+ it.xf_url
                            }
                        }
                    }else{
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
                                    ?: 0))
                }
            }
        }

    }
}