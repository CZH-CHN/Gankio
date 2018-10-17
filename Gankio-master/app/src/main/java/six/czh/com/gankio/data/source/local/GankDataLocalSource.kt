package six.czh.com.gankio.data.source.local

import android.util.Log
import six.czh.com.gankio.GankApplication
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.source.GankDataSource

/**
 * 数据库数据
 * Created by oneplus on 18-10-8.
 * Email: six.cai@oneplus.com
 */
class GankDataLocalSource {

    private val mDb = MySQLDB.getInstance(GankApplication.mContext)

    fun getGankData(callback: GankDataSource.LoadGankDataCallback) {
        val gankData = mDb.getGankDataFromDB()

        if(gankData.results.isEmpty()) {
            callback.onGankDataLoadedFail()
        } else {
            for(item in gankData.results) {
                Log.d("ccccccccccccccc", item.toString())
            }
            //回调 数据已经加载好
            callback.onGankDataLoaded(gankData)
        }


    }

    fun saveGankData(gankResults: List<GankResult>?) {
        mDb.saveGankData(gankResults)
    }

    companion object {

        private lateinit var INSTANCE: GankDataLocalSource
        private var needsNewInstance = true

        @JvmStatic fun getInstance(): GankDataLocalSource {
            if (needsNewInstance) {
                INSTANCE = GankDataLocalSource()
                needsNewInstance = false
            }
            return INSTANCE
        }
    }
}