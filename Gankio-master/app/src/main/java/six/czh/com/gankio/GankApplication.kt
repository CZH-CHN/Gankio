package six.czh.com.gankio

import android.app.Application
import android.content.Context

/**
 * Created by czh on 18-10-9.
 * Email: six.cai@czh.com
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