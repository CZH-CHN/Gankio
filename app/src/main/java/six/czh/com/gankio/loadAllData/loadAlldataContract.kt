package six.czh.com.myapplication.loadAllData

import six.czh.com.myapplication.BasePresenter
import six.czh.com.myapplication.BaseView

interface loadAlldataContract {

    interface View : BaseView<Presenter> {
        fun loadMsgSuccess()

        fun loadMsgFail()
    }

    interface Presenter : BasePresenter {
        fun loadMsg()


    }

}