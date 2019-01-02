package six.czh.com.gankio.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity(foreignKeys = [
    ForeignKey(
            entity = GankResult::class,
            parentColumns = arrayOf("_id"),
            childColumns = arrayOf("gankId"))
],tableName = "image")
@Parcelize
data class GankImage(@PrimaryKey(autoGenerate = true)  var _id: Int,
                 @ColumnInfo(name = "gankId") var gankId: Int,
                 @ColumnInfo(name = "url") var url: String):Parcelable