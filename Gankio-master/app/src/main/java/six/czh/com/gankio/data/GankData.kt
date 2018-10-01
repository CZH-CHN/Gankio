package six.czh.com.gankio.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GankData(
        val error: Boolean,
        val results: List<GankResult>
)
data class GankResult(
        val _id: String,
        val createdAt: String,
        val desc: String,
        val publishedAt: String,
        val source: String,
        val type: String,
        val url: String,
        val used: Boolean,
        val who: String
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(createdAt)
        parcel.writeString(desc)
        parcel.writeString(publishedAt)
        parcel.writeString(source)
        parcel.writeString(type)
        parcel.writeString(url)
        parcel.writeByte(if (used) 1 else 0)
        parcel.writeString(who)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GankResult> {
        override fun createFromParcel(parcel: Parcel): GankResult {
            return GankResult(parcel)
        }

        override fun newArray(size: Int): Array<GankResult?> {
            return arrayOfNulls(size)
        }
    }
}