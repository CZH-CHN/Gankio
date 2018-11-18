package six.czh.com.gankio.data.source

import android.arch.lifecycle.LiveData
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult

/**
 * Created by Administrator on 2018/8/25 0025.
 */
const val LOAD_DATA_IS_EMPTY = 1
const val LOAD_DIR_NO_EXISTED = 2
const val LOAD_NETWORK_ERROR = 3
const val LOAD_WRITE_FILE_ERROR = 4
const val LOAD_FILE_IS_EXISTS = 5
interface GankDataSource {

    interface LoadGankDataCallback{
        fun onGankDataLoaded(gankResultList : GankData?)

        fun onGankDataLoadedFail(errorCode: Int)
    }

    fun getGankData(topic : String, num : Int, page : Int, callback: LoadGankDataCallback?): LiveData<List<GankResult>>

    fun saveGankData(gankResultList : GankData?)

    fun getGankDataForLocal(callback: GankDataSource.LoadGankDataCallback)

}