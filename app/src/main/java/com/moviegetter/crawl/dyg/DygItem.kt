package com.moviegetter.crawl.dyg

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.dytt.DYTTItem



/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
@Entity(tableName = "dyg_table")
data class DygItem(
        @PrimaryKey
        val movieId: Int,
        val movieName: String, val movie_update_time: String?,val image:String?,
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
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        super.writeToParcel(parcel, flags)
        parcel.writeInt(movieId)
        parcel.writeString(movieName)
        parcel.writeString(movie_update_time)
        parcel.writeString(image)
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

    companion object CREATOR : Parcelable.Creator<DygItem> {
        override fun createFromParcel(parcel: Parcel): DygItem {
            return DygItem(parcel)
        }

        override fun newArray(size: Int): Array<DygItem?> {
            return arrayOfNulls(size)
        }
    }

    fun toDYTTItem(): DYTTItem {
        return DYTTItem(this.movieId, this.movieName, this.movie_update_time,
                this.richText, this.downloadName, this.downloadUrls, this.downloadThunder,
                this.update_time, this.create_time, this.movie_update_timestamp, this.position,
                this.downloaded, this.downloaded_time)
    }
}



