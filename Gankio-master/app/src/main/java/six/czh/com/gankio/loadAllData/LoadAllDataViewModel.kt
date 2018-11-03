package six.czh.com.gankio.loadAllData

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.content.Intent
import six.czh.com.gankio.SingleLiveEvent
import six.czh.com.gankio.UrlParams
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.Resource
import six.czh.com.gankio.data.source.GankDataRepository
import six.czh.com.gankio.detailData.detailDataActivity
import six.czh.com.gankio.util.AppExecutors

/**
 * Created by cai on 18-10-29.
 * Email: baicai94@foxmail.com
 */
class LoadAllDataViewModel(
        context: Application,
        private val repository: GankDataRepository) : AndroidViewModel(context) {

    private val TAG = LoadAllDataViewModel::class.java.simpleName

    val openDetailUi = SingleLiveEvent<detailUiData>()

    val detailActivityReener = SingleLiveEvent<Intent>()

    private val paramsInput = MutableLiveData<UrlParams>()

    var gankResults: LiveData<List<GankResult>> = Transformations.switchMap(paramsInput) {
        repository.getGankData(it.topic, it.num, it.page)
    }
    fun getGankData(topic : String, num : Int, page : Int) {
        paramsInput.value = UrlParams(topic, num, page)

    }


    fun openDetailUi(gankPhotos: List<GankResult>, position: Int) {
        openDetailUi.value = detailUiData(gankPhotos, position)
    }


    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == detailDataActivity.REQUEST_DETAIL_DATA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                detailActivityReener.value = data
            } else {
                //错误信息
            }
        }
    }

    data class detailUiData(val gankPhotos: List<GankResult>, val position: Int)

}