package six.czh.com.myapplication.loadAllData

import android.content.Intent
import six.czh.com.gankio.data.GankResult
import six.czh.com.myapplication.BasePresenter
import six.czh.com.myapplication.BaseView

interface LoadAlldataContract {

    interface View : BaseView<Presenter> {
        fun loadMsgSuccess(gankResultList : List<GankResult>)

        fun loadMsgFail()

        fun showAllData(data: Intent?)

        fun showDetailUi(gankPhotos : List<GankResult>, position: Int)
    }

    interface Presenter : BasePresenter {
        fun loadMsg(topic : String, num : Int, page : Int)

        //打开详情页面
        fun openDataDetails(gankPhotos : List<GankResult>, position: Int)

        fun result(requestCode: Int, resultCode: Int, data: Intent?)
    }

}