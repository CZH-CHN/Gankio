package six.czh.com.gankio.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import six.czh.com.gankio.data.GankResult

/**
 * Created by czh on 18-10-17.
 * Email: baicai94@foxmail.com six.cai@czh.com
 */
@Database(entities = arrayOf(GankResult::class), version = 1)
abstract class GankResultDatabase: RoomDatabase() {

    abstract fun gankDataDao(): GankResultDao

    companion object {

        private var INSTANCE: GankResultDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): GankResultDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    //创建数据库
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            GankResultDatabase::class.java, "data.db")
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}