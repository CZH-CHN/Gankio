package six.czh.com.gankio.data.source.local

import android.util.Log
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.source.GankDataSource
import six.czh.com.gankio.util.AppExecutors

/**
 * 数据库数据
 * Created by czh on 18-10-8.
 * Email: six.cai@czh.com
 */
class GankDataLocalSource(private val executor: AppExecutors, private val gankResultDao: GankResultDao) {


    fun getGankData(callback: GankDataSource.LoadGankDataCallback) {

        executor.diskIO.execute {
            val gankData = GankData(false, gankResultDao.getGankDataFromDB())

            executor.mainThread.execute {
                Log.d("ssssss", "size = " + gankData.results.size)
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
        }

    }

    fun saveGankData(gankResults: List<GankResult>?) {

        executor.diskIO.execute {
            if (gankResults != null) {
                for(item in gankResults) {
                    Log.d("ccccccccccccccc", item.toString())
                }
            }
            gankResultDao.saveGankDataToDB(gankResults)
        }

    }

    companion object {

        private lateinit var INSTANCE: GankDataLocalSource
        private var needsNewInstance = true

        @JvmStatic fun getInstance(executor: AppExecutors,gankResultDao: GankResultDao): GankDataLocalSource {
            if (needsNewInstance) {
                INSTANCE = GankDataLocalSource(executor,gankResultDao)
                needsNewInstance = false
            }
            return INSTANCE
        }
    }
}