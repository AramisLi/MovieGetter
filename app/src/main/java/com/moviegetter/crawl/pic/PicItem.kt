package com.moviegetter.crawl.pic

import android.os.Parcel
import android.os.Parcelable
import com.moviegetter.crawl.base.Item


/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
data class PicItem(val picId: Int, val picName: String, val pic_update_time: String?,
                   var pics: String? = null, var update_time: String? = null,
                   var create_time: String? = null, var pic_update_timestamp: Long = 0,
                   var thumb: String? = null, var position: Int? = null,
                   var watched: Int = 0, var watched_time: String? = null
) : Item {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(picId)
        parcel.writeString(picName)
        parcel.writeString(pic_update_time)
        parcel.writeString(pics)
        parcel.writeString(update_time)
        parcel.writeString(create_time)
        parcel.writeLong(pic_update_timestamp)
        parcel.writeString(thumb)
        parcel.writeValue(position)
        parcel.writeInt(watched)
        parcel.writeString(watched_time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PicItem> {
        override fun createFromParcel(parcel: Parcel): PicItem {
            return PicItem(parcel)
        }

        override fun newArray(size: Int): Array<PicItem?> {
            return arrayOfNulls(size)
        }
    }
}
