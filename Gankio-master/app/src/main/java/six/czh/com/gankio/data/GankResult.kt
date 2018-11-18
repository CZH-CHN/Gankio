package six.czh.com.gankio.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by czh on 18-10-17.
 * Email: six.cai@czh.com
 */
/**
 * Created by czh on 18-10-17.
 * Email: six.cai@czh.com
 */
@Entity(tableName = "fuli")
@Parcelize
data class GankResult(
        @PrimaryKey var _id: String = "",
        @ColumnInfo(name = "createdAt") var createdAt: String = "",
        @ColumnInfo(name = "desc") var desc: String = "",
        @ColumnInfo(name = "publishedAt") var publishedAt: String = "",
        @Ignore var images: List<String> = ArrayList(),
        @ColumnInfo(name = "source")  var source: String = "",
        @ColumnInfo(name = "type")  var type: String = "",
        @ColumnInfo(name = "url")  var url: String = "",
        @ColumnInfo(name = "used")  var used: Boolean = false,
        @ColumnInfo(name = "who")  var who: String? = ""
): Parcelable {

//    @Ignore constructor(parcel: Parcel) : this(
//            parcel.readString(),
//            parcel.readString(),
//            parcel.readString(),
//            parcel.readString(),
//            parcel.readArrayList(images, ArrayList::class.java.classLoader),
//            parcel.readString(),
//            parcel.readString(),
//            parcel.readString(),
//            parcel.readByte() != 0.toByte(),
//            parcel.readString())
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(_id)
//        parcel.writeString(createdAt)
//        parcel.writeString(desc)
//        parcel.writeString(publishedAt)
//        parcel.writeString(source)
//        parcel.writeString(type)
//        parcel.writeString(url)
//        parcel.writeByte(if (used) 1 else 0)
//        parcel.writeString(who)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<GankResult> {
//        override fun createFromParcel(parcel: Parcel): GankResult {
//            return GankResult(parcel)
//        }
//
//        override fun newArray(size: Int): Array<GankResult?> {
//            return arrayOfNulls(size)
//        }
//    }
}