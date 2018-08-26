package six.czh.com.gankio.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import six.czh.com.gankio.util.Contants
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Created by Administrator on 2018/8/25 0025.
 */
object ApiService {
    fun createRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
                .baseUrl(Contants.API_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

//        val service = retrofit.create(GankioService::class.java)

        return retrofit
    }

    private fun okHttpClient(): OkHttpClient {
        // config log
        return OkHttpClient.Builder()
                .connectTimeout((60 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .readTimeout((60 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .build()

    }
}