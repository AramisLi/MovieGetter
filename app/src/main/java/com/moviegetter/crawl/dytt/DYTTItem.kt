package com.moviegetter.crawl.dytt

import android.os.Parcel
import android.os.Parcelable
import com.moviegetter.crawl.base.Item


/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
data class DYTTItem(val movieId: Int, val movieName: String, val movie_update_time: String?,
                    var richText: String? = null, var downloadName: String? = null,
                    var downloadUrls: String? = null, var downloadThunder: String? = null,
                    var update_time: String? = null, var create_time: String? = null,
                    var movie_update_timestamp: Long = 0, var position: Int,
                    var downloaded: Int = 0, var downloaded_time: String? = null
) : Item {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(movieId)
        parcel.writeString(movieName)
        parcel.writeString(movie_update_time)
        parcel.writeString(richText)
        parcel.writeString(downloadName)
        parcel.writeString(downloadUrls)
        parcel.writeString(downloadThunder)
        parcel.writeString(update_time)
        parcel.writeString(create_time)
        parcel.writeLong(movie_update_timestamp)
        parcel.writeInt(position)
        parcel.writeInt(downloaded)
        parcel.writeString(downloaded_time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DYTTItem> {
        override fun createFromParcel(parcel: Parcel): DYTTItem {
            return DYTTItem(parcel)
        }

        override fun newArray(size: Int): Array<DYTTItem?> {
            return arrayOfNulls(size)
        }
    }
}
