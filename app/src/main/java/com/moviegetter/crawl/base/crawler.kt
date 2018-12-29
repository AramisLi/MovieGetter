package com.moviegetter.crawl.base

import android.content.Context
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
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
    fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline? = null): List<CrawlNode>?
//    fun preCheck(context: Context?, node: CrawlNode): Boolean
}

interface Pipeline {
    fun pipe(context: Context?, items: List<Item>, handler: Handler?, statusCallback: ((what: Int, obj: Any?) -> Unit)? = null)
}

interface Item : Serializable, Parcelable

class CrawlNode(val url: String, val level: Int, val parentNode: CrawlNode?,
                var childrenNodes: List<CrawlNode>?, var item: Item?, var isItem: Boolean,
                var tag: String? = null, var position: Int) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readParcelable(CrawlNode::class.java.classLoader),
            parcel.createTypedArrayList(CREATOR),
            parcel.readParcelable(Item::class.java.classLoader),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readInt()) {
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
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CrawlNode> {
        override fun createFromParcel(parcel: Parcel): CrawlNode {
            return CrawlNode(parcel)
        }

        override fun newArray(size: Int): Array<CrawlNode?> {
            return arrayOfNulls(size)
        }
    }
}


