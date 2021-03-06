package com.moviegetter.crawl.hu

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.moviegetter.crawl.base.Item

/**
 * Created by lizhidan on 2019/5/5.
 */
@Entity(tableName = "hu_table")
data class HuItem(@PrimaryKey val movieId: Int, val tagName:String, val movieName: String, var movie_update_time: String?,
                  var playUrl: String? = null,var downloadUrl: String? = null, var update_time: String? = null,
                  var create_time: String? = null, var movie_update_timestamp: Long = 0,
                  var thumb: String? = null, var images: String? = null, var position: Int? = null,
                  var downloaded: Int = 0, var downloaded_time: String? = null
):Item {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
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
//        super.writeToParcel(parcel, flags)
        parcel.writeInt(movieId)
        parcel.writeString(tagName)
        parcel.writeString(movieName)
        parcel.writeString(movie_update_time)
        parcel.writeString(playUrl)
        parcel.writeString(downloadUrl)
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

    companion object CREATOR : Parcelable.Creator<HuItem> {
        override fun createFromParcel(parcel: Parcel): HuItem {
            return HuItem(parcel)
        }

        override fun newArray(size: Int): Array<HuItem?> {
            return arrayOfNulls(size)
        }
    }
}