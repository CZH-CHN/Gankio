package six.czh.com.gankio

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary

/**
 * Created by czh on 18-10-9.
 * Email: baicai94@foxmail.com six.cai@czh.com
 */
class GankApplication: Application() {

    companion object {
        lateinit var mContext: Context
    }
    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        mContext = applicationContext
    }

    fun getContext(): Context = mContext
}