package six.czh.com.gankio.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import six.czh.com.gankio.data.download.GankDataDownload
import java.io.File

/**
 * Created by cai on 18-10-24.
 * Email: baicai94@foxmail.com
 */
class MediaUtils private constructor(){

    private var instance: MediaUtils? = null


    companion object {

        private lateinit var INSTANCE: MediaUtils
        private var needsNewInstance = true

        @JvmStatic fun getInstance(): MediaUtils {
            if (needsNewInstance) {
                INSTANCE = MediaUtils()
                needsNewInstance = false
            }
            return INSTANCE
        }
    }

    fun getImageContentUri(context: Context?, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context?.contentResolver!!.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                MediaStore.Images.Media.DATA + "=? ",
                arrayOf(filePath), null)
        cursor.use { cursor ->
            return if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.MediaColumns._ID))
                val baseUri = Uri.parse("content://media/external/images/media")
                Uri.withAppendedPath(baseUri, "" + id)
            } else {
                if (imageFile.exists()) {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.DATA, filePath)
                    context?.contentResolver!!.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                } else {
                    null
                }
            }
        }
    }
}