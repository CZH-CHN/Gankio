package six.czh.com.gankio.detailData

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.os.Environment
import six.czh.com.gankio.R
import six.czh.com.gankio.SingleLiveEvent
import six.czh.com.gankio.UrlParams
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.download.*
import six.czh.com.gankio.data.source.GankDataRepository
import six.czh.com.gankio.util.AppExecutors
import six.czh.com.gankio.util.LogUtils
import java.io.File

/**
 * Created by cai on 18-10-30.
 * Email: baicai94@foxmail.com
 */
class DetailDataViewModel(
        private val context: Application,
        private val repository: GankDataRepository): AndroidViewModel(context) {

    private val TAG = DetailDataViewModel::class.java.simpleName

    private val paramsInput = MutableLiveData<UrlParams>()

    val position = SingleLiveEvent<Int>()

    val downloadResult = MutableLiveData<DownloadImage>()

    val toastMessage = SingleLiveEvent<String>()

    var gankResults: LiveData<List<GankResult>> = Transformations.switchMap(paramsInput) {
        repository.getGankData(it.topic, it.num, it.page, null)
    }
    fun getGankData(topic : String, num : Int, page : Int) {
        paramsInput.value = UrlParams(topic, num, page)
    }

    fun showAllDataUi(itemPosition: Int) {
        position.value = itemPosition
    }

    fun setToastMessage(errorCode: Int) {

        toastMessage.value =
        when (errorCode) {
            DOWNLOAD_DIR_NO_EXISTED -> context.resources.getString(R.string.download_fail)
            DOWNLOAD_PERMISSION_DENIED -> context.resources.getString(R.string.download_fail_permission)
            DOWNLOAD_FILE_IS_EXISTS -> context.resources.getString(R.string.download_file_exist)
            DOWNLOAD_NETWORK_ERROR -> context.resources.getString(R.string.download_network_error)
            DOWNLOAD_WRITE_FILE_ERROR -> context.resources.getString(R.string.download_fail)
            else -> return
        }
    }

    data class DownloadImage(val file: File, val type: Int)
    /**
     * @params type 请求类型
     */
    fun downloadImage(uri: String, type: Int) {

        /**
         * 1.判断文件是否已经下载
         * 如果已经下载了就直接调用view的share接口
         * 如果还未下载则调用下载接口
         */
        GankDataDownload.getInstance(AppExecutors()).downloadImage(uri, type, object : GankDataDownloadSource.LoadGankDownloadCallback {
            override fun onGankDataDownloaded() {
                val fileName = uri.substring(uri.lastIndexOf('/') + 1)
                val file = File(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gank Fuli").absolutePath + File.separator + fileName)

                downloadResult.value = DownloadImage(file, type)
            }

            override fun onGankDataDownloadedFail(errorCode: Int) {
                val fileName = uri.substring(uri.lastIndexOf('/') + 1)
                val file = File(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gank Fuli").absolutePath + File.separator + fileName)

                if (errorCode == DOWNLOAD_FILE_IS_EXISTS) {
                    downloadResult.value = DownloadImage(file, type)
                }
                setToastMessage(errorCode)
                LogUtils.d("DetailDataPresenter", "errorCode = $errorCode")
            }

        })
    }
}