package six.czh.com.gankio.data

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Created by Administrator on 2018/8/25 0025.
 */
interface GankioService {

    @GET("data/{topic}/{num}/{page}")
    fun getGankData(@Path("topic") topic : String, @Path("num") num : Int,
                    @Path("page") page : Int) : Call<GankData>

    @Streaming
    @GET
    fun downloadImage(@Url url: String): Call<ResponseBody>
}