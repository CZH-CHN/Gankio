package six.czh.com.myapplication.loadAllData

import six.czh.com.gankio.data.GankResult
import six.czh.com.myapplication.BasePresenter
import six.czh.com.myapplication.BaseView

interface loadAlldataContract {

    interface View : BaseView<Presenter> {
        fun loadMsgSuccess(gankResultList : List<GankResult>)

        fun loadMsgFail()
    }

    interface Presenter : BasePresenter {
        fun loadMsg(topic : String, num : Int, page : Int)


    }

}