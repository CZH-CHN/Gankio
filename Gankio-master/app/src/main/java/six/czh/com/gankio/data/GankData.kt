package six.czh.com.gankio.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GankData(
        val error: Boolean = false,
        val results: List<GankResult> = emptyList()
) {
}
data class GankResult(
        var _id: String = "",
        var createdAt: String = "",
        var desc: String = "",
        var publishedAt: String = "",
        var source: String = "",
        var type: String = "",
        var url: String = "",
        var used: Boolean = false,
        var who: String = ""
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