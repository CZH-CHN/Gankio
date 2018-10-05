package six.czh.com.gankio.detailData

import six.czh.com.myapplication.BasePresenter
import six.czh.com.myapplication.BaseView

interface detailDataContract {

    interface View : BaseView<Presenter> {
        fun loadImageSuccess()

        fun loadImageFail()

        fun showAllData()

    }

    interface Presenter : BasePresenter {
        fun loadImage(url: String)

    }
}