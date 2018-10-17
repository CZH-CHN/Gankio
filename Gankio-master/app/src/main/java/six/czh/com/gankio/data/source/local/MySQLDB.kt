package six.czh.com.gankio.data.source.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.source.GankDataSource
import six.czh.com.gankio.util.LogUtils

/**
 * Created by oneplus on 18-10-9.
 * Email: six.cai@oneplus.com
 */
class MySQLDB(private val context: Context) {


    private val mDb: SQLiteDatabase

    private val mDbHelper: MySQLDatabaseHelper = MySQLDatabaseHelper(context)

    init {
        mDb = mDbHelper.writableDatabase
    }

    fun getGankDataFromDB(): GankData {

        val gankDatas = ArrayList<GankResult>()

        val cursor = mDb.rawQuery("SELECT * FROM $TABLE_NAME", null)

        LogUtils.d(" count = " + cursor.count )
        if(cursor == null || cursor.count < 1) {
            return GankData()
        }


        cursor.moveToFirst()
        do {
            var gankData = GankResult()
            gankData._id = cursor.getString(cursor.getColumnIndex("_id"))
            gankData.url = cursor.getString(cursor.getColumnIndex("url"))
            gankData.desc = cursor.getString(cursor.getColumnIndex("desc"))
            gankData.source = cursor.getString(cursor.getColumnIndex("source"))
            gankData.type = cursor.getString(cursor.getColumnIndex("type"))
            gankData.used = cursor.getInt(cursor.getColumnIndex("used")) == 1
            gankData.who = cursor.getString(cursor.getColumnIndex("who"))
            gankData.createdAt = cursor.getString(cursor.getColumnIndex("createdAt"))
            gankData.publishedAt = cursor.getString(cursor.getColumnIndex("publishedAt"))
            LogUtils.d(gankData.toString())
            gankDatas.add(gankData)
        } while (cursor.moveToNext())
        cursor.close()
        //回调
        return GankData(false, gankDatas)
    }

    fun saveGankData(gankResults: List<GankResult>?) {
        if(gankResults == null || gankResults.isEmpty()) {
            return
        }
        LogUtils.d("size = " + gankResults.size)
//        mDb.beginTransaction()
        try {
            for (item in gankResults) {
                var values = ContentValues()
                values.put("_id", item._id)
                values.put("url", item.url)
                values.put("source", item.source)
                values.put("type", item.type)
                values.put("desc", item.desc)
                values.put("used", item.used)
                values.put("who", item.who)
                values.put("createdAt", item.createdAt)
                values.put("publishedAt", item.publishedAt)
                mDb.insert(TABLE_NAME, null, values)

            }
        } finally {
//            mDb.endTransaction()
        }


    }

    fun clearDBData() {
        mDb.execSQL("DELETE FROM $TABLE_NAME")
    }

    companion object {

        private lateinit var INSTANCE: MySQLDB
        private var needsNewInstance = true

        @JvmStatic fun getInstance(context: Context): MySQLDB {
            if (needsNewInstance) {
                INSTANCE = MySQLDB(context)
                needsNewInstance = false
            }
            return INSTANCE
        }
    }
}