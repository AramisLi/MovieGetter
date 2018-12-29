package com.moviegetter.crawl.ipz

import android.os.Parcel
import android.os.Parcelable
import com.moviegetter.crawl.base.Item


/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
data class IPZItem(val movieId: Int, val movieName: String, var movie_update_time: String?,
                   var xf_url: String? = null, var update_time: String? = null,
                   var create_time: String? = null, var movie_update_timestamp: Long = 0,
                   var thumb: String? = null, var images: String? = null, var position: Int? = null,
                   var downloaded: Int = 0, var downloaded_time: String? = null
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
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(movieId)
        parcel.writeString(movieName)
        parcel.writeString(movie_update_time)
        parcel.writeString(xf_url)
        parcel.writeString(update_time)
        parcel.writeString(create_time)
        parcel.writeLong(movie_update_timestamp)
        parcel.writeString(thumb)
        parcel.writeString(images)
        parcel.writeValue(position)
        parcel.writeInt(downloaded)
        parcel.writeString(downloaded_time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IPZItem> {
        override fun createFromParcel(parcel: Parcel): IPZItem {
            return IPZItem(parcel)
        }

        override fun newArray(size: Int): Array<IPZItem?> {
            return arrayOfNulls(size)
        }
    }
}
