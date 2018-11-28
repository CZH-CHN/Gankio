package six.czh.com.gankio.util

import android.util.Log

/**
 * Created by czh on 18-9-25.
 * Email: baicai94@foxmail.com caichelin@gmail.com
 */
object LogUtils {
    val LOGTAG = "CZHGankio"

    /**
     *
     */
    fun d(tag: String, message: String, vararg args: Any) {
        Log.d(LOGTAG + "/" + tag,
        if (args.size == 0)
            message
        else
            String.format(
                    message, *args))
    }

    fun d(tag: String, message: String, e: Exception) {
        Log.d("$LOGTAG/$tag", message, e)
    }

    fun d(message: String, vararg args: Any) {
        Log.d(LOGTAG,
                if (args.size == 0)
                    message
                else
                    String.format(
                            message, *args))
    }

}