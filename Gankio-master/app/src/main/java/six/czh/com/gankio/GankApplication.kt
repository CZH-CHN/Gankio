package six.czh.com.gankio

import android.app.Application
import android.content.Context

/**
 * Created by oneplus on 18-10-9.
 * Email: six.cai@oneplus.com
 */
class GankApplication: Application() {

    companion object {
        lateinit var mContext: Context
    }
    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
    }

    fun getContext(): Context = mContext
}