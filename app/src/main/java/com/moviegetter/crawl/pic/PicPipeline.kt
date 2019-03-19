package com.moviegetter.crawl.pic

import android.content.Context
import com.aramis.library.extentions.getTimestamp
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.BasePipeline
import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.ipz.IPZItem
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
class PicPipeline : BasePipeline() {
    override fun pipeHook(context: Context?, items: List<Item>) {
        logE("保存item:$items")
        context?.database?.use {
            items.filter { it is PicItem }.map { it as PicItem }.forEach {
                if (select(DBConfig.TABLE_NAME_PIC).whereArgs("picId = \"${it.picId}\"").exec { this.count } > 0) {
                    logE("图片 update update update position:${it.position}")

                    update(DBConfig.TABLE_NAME_PIC,
                            "picName" to it.picName,
                            "pic_update_time" to it.pic_update_time,
                            "pics" to it.pics,
                            "update_time" to now(),
                            "pic_update_timestamp" to (it.pic_update_time?.getTimestamp("yyyy-MM-dd")
                                    ?: 0)).whereArgs("picId = {picId}", "picId" to it.picId).exec()
                } else {
                    logE("图片 insert insert insert ${it.position}")

//                    PicItem(val picId: Int, val picName: String, val pic_update_time: String?,
//                    var pics: String? = null, var update_time: String? = null,
//                    var create_time: String? = null, var pic_update_timestamp: Long = 0,
//                    var thumb: String? = null, var position: Int? = null,
//                    var watched: Int = 0, var watched_time: String? = null
//                    )
                    insert(DBConfig.TABLE_NAME_PIC,
                            "picId" to it.picId,
                            "picName" to it.picName,
                            "pic_update_time" to it.pic_update_time,
                            "pics" to it.pics,
                            "update_time" to now(),
                            "create_time" to now(),
                            "pic_update_timestamp" to (it.pic_update_time?.getTimestamp("yyyy-MM-dd")
                                    ?: 0),
                            "thumb" to it.thumb,
                            "position" to it.position,
                            "watched" to 0,
                            "watched_time" to null)
                }
            }
        }

    }


}