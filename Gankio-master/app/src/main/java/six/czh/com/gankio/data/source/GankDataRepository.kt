package six.czh.com.gankio.data.source

import retrofit2.Call
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.source.local.GankDataLocalSource
import six.czh.com.gankio.data.source.remote.GankDataRemoteSource
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by czh on 18-10-8.
 * Email: six.cai@czh.com
 */
class GankDataRepository(private val mGankDataRemoteSource: GankDataRemoteSource, private val mGankDataLocalSource: GankDataLocalSource): GankDataSource {
    //获取数据
    override fun getGankData(topic : String, num : Int, page : Int, callback: GankDataSource.LoadGankDataCallback) {
        //远端的数据需要存储到本地
        val gankResultCall = mGankDataRemoteSource.getGankData(topic, num, page, callback)

        gankResultCall.enqueue(object : Callback<GankData> {
            override fun onResponse(call: Call<GankData>?, response: Response<GankData>?) {
                var datalist = response?.body()
                /**
                 * 当返回数据有误时，回调
                 */
                if(datalist == null || datalist.error.equals(true)) {
                    callback.onGankDataLoadedFail()
                }
                //保存网络中获取到的数据
                saveGankData(datalist)
                mGankDataLocalSource.getGankData(callback)
            }

            override fun onFailure(call: Call<GankData>?, t: Throwable?) {
                mGankDataLocalSource.getGankData(callback)
                callback.onGankDataLoadedFail()
            }

        })
    }

    override fun saveGankData(gankResultList: GankData?) {

        mGankDataLocalSource.saveGankData(gankResultList?.results)
    }

    companion object {

        private var INSTANCE: GankDataRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param ganksRemoteDataSource the backend data source
         * *
         * @param gankDataLocalSource  the device storage data source
         * *
         * @return the [GankDataRepository] instance
         */
        @JvmStatic fun getInstance(ganksRemoteDataSource: GankDataRemoteSource, gankDataLocalSource: GankDataLocalSource): GankDataRepository {
            return INSTANCE ?: GankDataRepository(ganksRemoteDataSource, gankDataLocalSource)
                    .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }


}