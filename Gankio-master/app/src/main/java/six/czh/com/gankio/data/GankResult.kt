package six.czh.com.gankio.data

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by czh on 18-10-17.
 * Email: baicai94@foxmail.com six.cai@czh.com
 */
/**
 * Created by czh on 18-10-17.
 * Email: baicai94@foxmail.com six.cai@czh.com
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
}

