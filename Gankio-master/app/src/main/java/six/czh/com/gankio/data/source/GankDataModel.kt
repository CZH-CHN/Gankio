package six.czh.com.gankio.data.source

import android.os.AsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import six.czh.com.gankio.data.ApiService
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankioService

/**
 * Created by Administrator on 2018/8/25 0025.
 */
class GankDataModel {

    fun getGankData(topic : String, num : Int, page : Int, callback : GankDataSource.LoadGankDataCallback){
        val mGankioService = ApiService.createRetrofit().create(GankioService::class.java)
        val gankResultCall = mGankioService.getGankData(topic, num, page)

        gankResultCall.enqueue(object : Callback<GankData>{
            override fun onResponse(call: Call<GankData>?, response: Response<GankData>?) {
                var datalist = response?.body()

                if(datalist == null || datalist.error.equals(true)){
                    callback.onGankDataLoadedFail()
                }
                callback.onGankDataLoaded(datalist)
            }

            override fun onFailure(call: Call<GankData>?, t: Throwable?) {
                callback.onGankDataLoadedFail()
            }

        })
    }


}