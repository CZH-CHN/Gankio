package six.czh.com.myapplication.loadAllData

import android.app.Activity
import android.content.Intent
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.source.GankDataModel
import six.czh.com.gankio.data.source.GankDataSource
import six.czh.com.gankio.detailData.detailDataActivity

class loadAlldataPresenter(gankioDataView: loadAlldataContract.View) : loadAlldataContract.Presenter, GankDataSource.LoadGankDataCallback {
    override fun result(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == detailDataActivity.REQUEST_DETAIL_DATA && resultCode == Activity.RESULT_OK) {
            mGankioDataView.showAllData(data)
        }
    }

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

    /**
     * 打开详情页面
     */
    override fun openDataDetails(gankPhotos : List<GankResult>, position: Int) {
        mGankioDataView.showDetailUi(gankPhotos, position)
    }

}