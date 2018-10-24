package six.czh.com.gankio.detailData

import six.czh.com.gankio.data.download.GankDataDownloadSource
import six.czh.com.myapplication.BasePresenter
import six.czh.com.myapplication.BaseView
import java.io.File

interface detailDataContract {

    interface View : BaseView<Presenter> {
        fun loadImageSuccess()

        fun loadImageFail()

        fun showAllData()

        fun showSaveImageSuccess(file: File)

        fun showSaveImageFailed(errorCode: Int)

        fun startShareActivity(file: File)
    }

    interface Presenter : BasePresenter {
        fun loadImage(url: String)

        fun saveImage(uri: String)

        fun shareImage(uri: String)

    }
}