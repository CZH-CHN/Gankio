package six.czh.com.gankio.data.download

import six.czh.com.gankio.data.GankData
import java.io.File

/**
 * Created by cai on 18-10-22.
 * Email: baicai94@foxmail.com
 */
const val DOWNLOAD_PERMISSION_DENIED = 1
const val DOWNLOAD_DIR_NO_EXISTED = 2
const val DOWNLOAD_NETWORK_ERROR = 3
const val DOWNLOAD_WRITE_FILE_ERROR = 4
const val DOWNLOAD_FILE_IS_EXISTS = 5
interface GankDataDownloadSource {

    interface LoadGankDownloadCallback{
        fun onGankDataDownloaded()

        fun onGankDataDownloadedFail(errorCode: Int)
    }

    fun downloadImage(uri: String, callback: LoadGankDownloadCallback)
}