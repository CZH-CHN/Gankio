package six.czh.com.gankio.util

import android.app.Service
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by czh on 18-9-26.
 * Email: caichelin@gmail.com
 */
object UIUtils {
    fun getWindowWidth(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metric = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metric)
        return metric.heightPixels     // 窗口宽度（像素）


    }

    fun getDefaultScale(context: Context, imageWidth: Int): Int {
        if (imageWidth <= 0.0f) {
            throw IllegalArgumentException("can not be 0.0f")
        }
        return imageWidth * ((getWindowWidth(context) / imageWidth))
    }

}