package six.czh.com.gankio.data.source.local

import android.arch.lifecycle.LiveData
import android.util.Log
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.GankioData
import six.czh.com.gankio.data.source.GankDataSource
import six.czh.com.gankio.data.source.LOAD_DATA_IS_EMPTY
import six.czh.com.gankio.util.AppExecutors
import six.czh.com.gankio.util.LogUtils

/**
 * 数据库数据
 * Created by czh on 18-10-8.
 * Email: baicai94@foxmail.com six.cai@czh.com
 */
class GankDataLocalSource(private val executor: AppExecutors, private val gankResultDao: GankResultDao) {


    fun getGankData(type: String, callback: GankDataSource.LoadGankDataCallback) {

        executor.diskIO.execute {
            val gankData = GankData(false, gankResultDao.getGankioDataFromDBByType(type))

            executor.mainThread.execute {
                Log.d("ssssss", "size = " + gankData.results.size)
                if(gankData.results.isEmpty()) {
                    callback.onGankDataLoadedFail(LOAD_DATA_IS_EMPTY)
                } else {
                    for(item in gankData.results) {
//                        Log.d("ccccccccccccccc", item.toString())
                    }
                    //回调 数据已经加载好
                    callback.onGankDataLoaded(gankData)


                }
            }
        }

    }


    fun saveGankData(gankioDatas: List<GankioData>?) {

        executor.diskIO.execute {

//            if (gankResults != null) {
//                for(item in gankResults) {
//                    Log.d("ccccccccccccccc", "who = " + item.who)
//                }
//            }
            try {
                for (data in gankioDatas!!) {
                    gankResultDao.saveGankDataToDB_1(data)
                }

//                var values = gankResultDao.saveGankDataToDB(gankResults)

//                Log.d("ccccccccccccccc", "change_size = " + values.size)
            } catch (e: Exception) {
                e.printStackTrace()
            }

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




//    fun getGankData(type: String): List<GankResult> {
//        executor.diskIO.execute {
//            val gankData = gankResultDao.getGankDataFromDBNormal(type)
//            executor.mainThread.execute {
//            }
//        }
//
//    }
}