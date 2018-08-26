package six.czh.com.myapplication.loadAllData

import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.source.GankDataModel
import six.czh.com.gankio.data.source.GankDataSource

class loadAlldataPresenter(gankioDataView: loadAlldataContract.View) : loadAlldataContract.Presenter, GankDataSource.LoadGankDataCallback {

    private val mGankioDataView : loadAlldataContract.View = gankioDataView;

    private val mGankioDataModel : GankDataModel = GankDataModel();


    override fun onGankDataLoaded(gankResultList: GankData?) {
        mGankioDataView.loadMsgSuccess(gankResultList!!.results)
    }

    override fun onGankDataLoadedFail() {
        mGankioDataView.loadMsgFail()
    }

    //加载
    override fun loadMsg(topic : String, num : Int, page : Int) {
        mGankioDataModel.getGankData(topic, num, page, this)
    }

    override fun start() {
    }

}