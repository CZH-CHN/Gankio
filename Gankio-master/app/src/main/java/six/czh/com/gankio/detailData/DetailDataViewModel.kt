package six.czh.com.gankio.detailData

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.os.Environment
import six.czh.com.gankio.SingleLiveEvent
import six.czh.com.gankio.UrlParams
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.download.GankDataDownload
import six.czh.com.gankio.data.download.GankDataDownloadSource
import six.czh.com.gankio.data.source.GankDataRepository
import six.czh.com.gankio.util.AppExecutors
import six.czh.com.gankio.util.LogUtils
import java.io.File

/**
 * Created by cai on 18-10-30.
 * Email: baicai94@foxmail.com
 */
class DetailDataViewModel(
        context: Application,
        private val repository: GankDataRepository): AndroidViewModel(context) {

    private val TAG = DetailDataViewModel::class.java.simpleName

    private val paramsInput = MutableLiveData<UrlParams>()

    val position = SingleLiveEvent<Int>()

    val downloadResult = MutableLiveData<DownloadImage>()

    var gankResults: LiveData<List<GankResult>> = Transformations.switchMap(paramsInput) {
        repository.getGankData(it.topic, it.num, it.page)
    }
    fun getGankData(topic : String, num : Int, page : Int) {
        paramsInput.value = UrlParams(topic, num, page)
    }

    fun showAllDataUi(itemPosition: Int) {
        position.value = itemPosition
    }

    data class DownloadImage(val file: File, val type: Int)
    /**
     * @params type 请求类型
     */
    fun downloadImage(uri: String, type: Int) {
        GankDataDownload.getInstance(AppExecutors()).downloadImage(uri, type, object : GankDataDownloadSource.LoadGankDownloadCallback {
            override fun onGankDataDownloaded() {
                val fileName = uri.substring(uri.lastIndexOf('/') + 1)
                val file = File(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gank Fuli").absolutePath + File.separator + fileName)

                downloadResult.value = DownloadImage(file, type)
//                detailDataView.showSaveImageSuccess(file)
            }

            override fun onGankDataDownloadedFail(errorCode: Int) {
//                detailDataView.showSaveImageFailed(errorCode)
                LogUtils.d("DetailDataPresenter", "errorCode = $errorCode")
            }

        })
    }
}