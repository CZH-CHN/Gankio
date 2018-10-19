package six.czh.com.gankio.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult

/**
 * Created by czh on 18-10-17.
 * Email: six.cai@czh.com
 */
@Dao
interface GankResultDao {

    @Query("SELECT * FROM fuli")
    fun getGankDataFromDB(): List<GankResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGankDataToDB(gankResults: List<GankResult>?)
}