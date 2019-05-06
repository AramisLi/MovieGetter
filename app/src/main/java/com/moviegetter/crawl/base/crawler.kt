package com.moviegetter.crawl.base

import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import com.moviegetter.config.MovieConfig
import com.moviegetter.db.MovieDatabase
import java.io.Serializable

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
interface Crawler {
    fun startCrawl(context: Context?, url: String, parser: Parser?, pipeline: Pipeline?, handler: Handler?)
    fun isRunning(): Boolean
}

interface Parser {
    var pages: Int
    val tag: String
    fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline? = null): List<CrawlNode>?
    /**
     * 跳过条件，满足条件将跳过
     * true:跳过
     * false:不跳过
     */
    fun skipCondition(database: MovieDatabase, node: CrawlNode): Boolean
}

interface Pipeline {
    fun pipe(context: Context?, items: List<Item>, handler: Handler?, statusCallback: ((what: Int, obj: Any?) -> Unit)? = null)
}

interface Item : Serializable, Parcelable

class CrawlNode(val url: String, val level: Int, val parentNode: CrawlNode?,
                var childrenNodes: List<CrawlNode>?, var item: Item?, var isItem: Boolean,
                var tag: String? = null, var position: Int, var positionName: String = "") : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readParcelable(CrawlNode::class.java.classLoader),
            parcel.createTypedArrayList(CREATOR),
            parcel.readParcelable(Item::class.java.classLoader),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeInt(level)
        parcel.writeParcelable(parentNode, flags)
        parcel.writeTypedList(childrenNodes)
        parcel.writeParcelable(item, flags)
        parcel.writeByte(if (isItem) 1 else 0)
        parcel.writeString(tag)
        parcel.writeInt(position)
        parcel.writeString(positionName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CrawlNode> {
        val LEVEL_ROOT = 0
        val LEVEL_LIST = 1
        val LEVEL_DETAIL = 2
        override fun createFromParcel(parcel: Parcel): CrawlNode {
            return CrawlNode(parcel)
        }

        override fun newArray(size: Int): Array<CrawlNode?> {
            return arrayOfNulls(size)
        }

        fun createRootNode(url: String, tag: String?, position: Int = 0): CrawlNode {
            return CrawlNode(url, LEVEL_ROOT, null, null, null, false, tag, position)
        }

        fun createListNode(url: String, parentNode: CrawlNode?, item: Item?, positionName: String = ""): CrawlNode {
            return CrawlNode(url, LEVEL_LIST, parentNode, null, item, false, parentNode?.tag, parentNode?.position
                    ?: 2, positionName)
        }

        fun createDetailNode(url: String, parentNode: CrawlNode, item: Item): CrawlNode {
            return CrawlNode(url, LEVEL_DETAIL, parentNode, null, item, false, parentNode.tag, parentNode.position
                    , parentNode.positionName)
        }

    }


}


