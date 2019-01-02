package six.czh.com.gankio.data.source.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankImage
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.GankioData

/**
 * Created by czh on 18-10-17.
 * Email: baicai94@foxmail.com six.cai@czh.com
 */
@Dao
interface GankResultDao {

    @Query("SELECT * FROM fuli ORDER BY createdAt DESC")
    fun getGankDataFromDB(): List<GankResult>

    @Query("SELECT * FROM fuli WHERE type LIKE :type ORDER BY createdAt DESC")
    fun getGankDataFromDBNormal(type: String): List<GankResult>

    @Query("SELECT * FROM fuli ORDER BY createdAt DESC")
    fun getGankDataFromDB_normal(): LiveData<List<GankResult>>

    @Query("SELECT * FROM fuli WHERE type LIKE :type ORDER BY createdAt DESC")
    fun getGankDataFromDBByType(type: String): LiveData<List<GankResult>>

    /**
     * 插入语句
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGankDataToDB(gankResults: List<GankResult>?): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGankResult(gankResults: GankResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGankImage(gankImages: List<GankImage>?): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGankDataToDB_1(gankioData: GankioData) {
        insertGankResult(gankioData.gankResult)
        insertGankImage(gankioData.images)
    }

    /**
     * 查找语句
     */
    @Query("SELECT * from fuli INNER JOIN image ON fuli._id=image.gankId")
    fun getGankioDataFromDBByType(type: String): List<GankioData>
}