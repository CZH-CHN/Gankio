package six.czh.com.gankio.util

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by cai on 18-10-29.
 * Email: baicai94@foxmail.com
 */
const val PAGE = "SP_KEY_PAGE"
object PrefUtils {

    fun applyInt(context: Context?, key: String, value: Int) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(context: Context?, key: String, defaultValue: Int): Int? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getInt(key, defaultValue)
    }

}