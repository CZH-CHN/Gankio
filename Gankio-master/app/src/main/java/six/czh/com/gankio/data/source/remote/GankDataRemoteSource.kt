package six.czh.com.gankio.data.source.remote

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import six.czh.com.gankio.data.ApiService
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankioService
import six.czh.com.gankio.data.source.GankDataSource

/**
 * Created by czh on 18-10-8.
 * Email: baicai94@foxmail.com six.cai@czh.com
 */
class GankDataRemoteSource {

    //TODO 应当通过回调与repository通信。不应该返回具体实例给repository
    fun getGankData(topic : String, num : Int, page : Int, callback: GankDataSource.LoadGankDataCallback?) : Call<GankData> {

        val mGankioService = ApiService.createRetrofit().create(GankioService::class.java)
        return mGankioService.getGankData(topic, num, page)

    }


    companion object {

        private lateinit var INSTANCE: GankDataRemoteSource
        private var needsNewInstance = true

        @JvmStatic fun getInstance(): GankDataRemoteSource {
            if (needsNewInstance) {
                INSTANCE = GankDataRemoteSource()
                needsNewInstance = false
            }
            return INSTANCE
        }
    }

}