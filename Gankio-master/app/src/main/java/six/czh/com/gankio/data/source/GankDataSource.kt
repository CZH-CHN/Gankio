package six.czh.com.gankio.data.source

import six.czh.com.gankio.data.GankData

/**
 * Created by Administrator on 2018/8/25 0025.
 */
interface GankDataSource {

    interface LoadGankDataCallback{
        fun onGankDataLoaded(gankResultList : GankData?)

        fun onGankDataLoadedFail()
    }

    fun getGankData(topic : String, num : Int, page : Int, callback: LoadGankDataCallback)

    fun saveGankData(gankResultList : GankData?)

}