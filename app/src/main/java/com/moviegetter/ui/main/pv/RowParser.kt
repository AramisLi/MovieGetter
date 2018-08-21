package com.moviegetter.ui.main.pv

import com.aramis.library.extentions.logE
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.crawl.pic.PicItem
import org.jetbrains.anko.db.RowParser

/**
 *Created by Aramis
 *Date:2018/7/12
 *Description:
 */
class IPZPicRowParser : RowParser<PicItem> {
    override fun parseRow(columns: Array<Any?>): PicItem {
//        logE("size:"+columns.size.toString())
//        columns.forEachIndexed { index, any ->
//            logE("index:$index,${any?.toString()}")
//        }
        return PicItem(
                //picId
                (columns[0] as Long).toInt(),
                //picName
                columns[1] as String,
                //pic_update_time
                columns[2] as String,
                //pics
                columns[3] as? String?,
                //update_time
                columns[4] as? String?,
                //create_time
                columns[5] as? String?,
                //pic_update_timestamp
                (columns[6] as? Long ?: 0L),
                //thumb
                columns[7] as? String?,
                //position
                (columns[8] as Long).toInt(),
                //watched
                (columns[9] as Long).toInt(),
                //watched_time
                columns[10] as? String?)
    }

}
class IPZRowParser : RowParser<IPZItem> {
    override fun parseRow(columns: Array<Any?>): IPZItem {
//        logE("columns.size:${columns.size}")
        return IPZItem(
                //movieId
                (columns[0] as Long).toInt(),
                //movieName
                columns[1] as String,
                //movie_update_time
                columns[2] as String,
                //xf_url
                columns[3] as? String?,
                //update_time
                columns[4] as? String?,
                //create_time
                columns[5] as? String?,
                //movie_update_timestamp
                (columns[6] as? Long ?: 0L),
                //thumb
                columns[7] as? String?,
                //images
                columns[8] as? String?,
                //position
                (columns[9] as Long).toInt(),
                //downloaded
                (columns[10] as Long).toInt(),
                //downloaded_time
                columns[11] as? String?)
    }
}

class DYTTRowParser(val position: Int) : RowParser<DYTTItem> {
    override fun parseRow(columns: Array<Any?>): DYTTItem {

        return DYTTItem((columns[0] as Long).toInt(), columns[1] as String,

                columns[2]as? String, columns[3]as? String,
                columns[4]as? String, columns[5]as? String,
                columns[6]as? String, columns[7]as? String,
                columns[8]as? String, (columns[9]as? Long ?: 0L),
                position,
                (columns[10] as Long).toInt(), columns[11] as? String?)
    }

}