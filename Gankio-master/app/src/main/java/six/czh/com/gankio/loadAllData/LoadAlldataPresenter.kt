package six.czh.com.gankio.loadAllData

import android.app.Activity
import android.content.Intent
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.source.GankDataRepository
import six.czh.com.gankio.data.source.GankDataSource
import six.czh.com.gankio.detailData.detailDataActivity
import six.czh.com.gankio.util.UIUtils
import six.czh.com.myapplication.loadAllData.LoadAlldataContract

class LoadAlldataPresenter(val mGankDataRepository: GankDataRepository,
                           val mGankioDataView: LoadAlldataContract.View)
    : LoadAlldataContract.Presenter {
    override fun result(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == detailDataActivity.REQUEST_DETAIL_DATA && resultCode == Activity.RESULT_OK) {
            mGankioDataView.showAllData(data)
        }
    }

    //加载
    override fun loadMsg(topic: String, num: Int, page: Int) {
        //回调
        val callbacks = object : GankDataSource.LoadGankDataCallback {
            override fun onGankDataLoaded(gankResultList: GankData?) {
                mGankioDataView.loadMsgSuccess(gankResultList!!.results)
            }

            override fun onGankDataLoadedFail() {
                mGankioDataView.loadMsgFail()
            }
        }

        mGankDataRepository.getGankData(topic, num, page, callbacks)
    }

    override fun start() {
    }

    /**
     * 打开详情页面
     */
    override fun openDataDetails(gankPhotos: List<GankResult>, position: Int) {
        mGankioDataView.showDetailUi(gankPhotos, position)
    }

}