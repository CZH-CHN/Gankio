package six.czh.com.gankio.detailData

import android.os.Environment
import six.czh.com.gankio.data.download.DOWNLOAD_FILE_IS_EXISTS
import six.czh.com.gankio.data.download.GankDataDownload
import six.czh.com.gankio.data.download.GankDataDownloadSource
import six.czh.com.gankio.util.LogUtils
import java.io.File


class DetailDataPresenter(val dataDownload: GankDataDownload, val detailDataView: detailDataContract.View): detailDataContract.Presenter {
    //保存图片
    override fun saveImage(uri: String) {
        dataDownload.downloadImage(uri, object : GankDataDownloadSource.LoadGankDownloadCallback {
            override fun onGankDataDownloaded() {
                val fileName = uri.substring(uri.lastIndexOf('/') + 1)

                val file = File(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gank Fuli").absolutePath + File.separator + fileName)
                detailDataView.showSaveImageSuccess(file)
            }

            override fun onGankDataDownloadedFail(errorCode: Int) {
                detailDataView.showSaveImageFailed(errorCode)
                LogUtils.d("DetailDataPresenter", "errorCode = $errorCode")
            }

        })
    }

    //分享图片
    override fun shareImage(uri: String) {
        /**
         * 1.判断文件是否已经下载
         * 如果已经下载了就直接调用view的share接口
         * 如果还未下载则调用下载接口
         */
        dataDownload.downloadImage(uri, object : GankDataDownloadSource.LoadGankDownloadCallback {
            override fun onGankDataDownloaded() {
                val fileName = uri.substring(uri.lastIndexOf('/') + 1)

                val file = File(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gank Fuli").absolutePath + File.separator + fileName)
                detailDataView.showSaveImageSuccess(file)
                detailDataView.startShareActivity(file)
            }

            override fun onGankDataDownloadedFail(errorCode: Int) {
                //如果文件已经存在则直接
                if (errorCode == DOWNLOAD_FILE_IS_EXISTS) {
                    val fileName = uri.substring(uri.lastIndexOf('/') + 1)

                    val file = File(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gank Fuli").absolutePath + File.separator + fileName)
                    detailDataView.startShareActivity(file)
                } else {
                    detailDataView.showSaveImageFailed(errorCode)
                }

                LogUtils.d("DetailDataPresenter", "errorCode = $errorCode")
            }

        })

    }

    override fun loadImage(url: String) {
    }

    override fun start() {
    }
}