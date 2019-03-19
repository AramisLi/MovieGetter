package com.moviegetter.crawl.tv

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.Item

/**
 * Created by lizhidan on 2019/1/29.
 */
@Entity(tableName = DBConfig.TABLE_NAME_TV)
data class TvItem(@PrimaryKey val id: Int,
                  val cyId: Int, val cyName: String, val cyImage: String?,
                  val name: String, val detailUrl: String, var sourceUrl: String? = null) : Item {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(cyId)
        parcel.writeString(cyName)
        parcel.writeString(cyImage)
        parcel.writeString(name)
        parcel.writeString(detailUrl)
        parcel.writeString(sourceUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TvItem> {
        override fun createFromParcel(parcel: Parcel): TvItem {
            return TvItem(parcel)
        }

        override fun newArray(size: Int): Array<TvItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class TvItemWrapper(val cyId: Int, val cyName: String, val cyImage: String?, val items: MutableList<TvItem>) {

    fun addItem(item: TvItem) {
        if (item.cyId == cyId && item !in items) {
            items.add(item)
        }
    }
}