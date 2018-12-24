package six.czh.com.gankio.loadAllData

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.content.Intent
import six.czh.com.gankio.R
import six.czh.com.gankio.SingleLiveEvent
import six.czh.com.gankio.UrlParams
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.source.GankDataRepository
import six.czh.com.gankio.data.source.GankDataSource
import six.czh.com.gankio.data.source.LOAD_DATA_IS_EMPTY
import six.czh.com.gankio.data.source.LOAD_NETWORK_ERROR
import six.czh.com.gankio.detailData.DetailDataActivity

/**
 * Created by cai on 18-10-29.
 * Email: baicai94@foxmail.com
 */
class LoadAllDataViewModel(
        private val context: Application,
        private val repository: GankDataRepository) : AndroidViewModel(context) {

    private val TAG = LoadAllDataViewModel::class.java.simpleName

    val openDetailUi = SingleLiveEvent<detailUiData>()

    val detailActivityReener = SingleLiveEvent<Intent>()

    private val paramsInput = MutableLiveData<UrlParams>()

    val errorMessage = SingleLiveEvent<String>()

    var gankResults = MutableLiveData<List<GankResult>>()

    fun getGankData(topic : String, num : Int, page : Int) {
        repository.getGankData(topic, num, page, object : GankDataSource.LoadGankDataCallback {
            override fun onGankDataLoaded(gankResultList: GankData?) {
                gankResults.value = gankResultList?.results
            }

            override fun onGankDataLoadedFail(errorCode: Int) {
                setErrorMessage(errorCode)
            }

        })

    }

    fun setErrorMessage(errorCode: Int) {
        errorMessage.value =
            when (errorCode) {
                LOAD_DATA_IS_EMPTY -> context.resources.getString(R.string.load_data_empty)
                LOAD_NETWORK_ERROR -> context.resources.getString(R.string.download_network_error)
                else -> return
            }
    }

    fun openDetailUi(gankPhotos: List<GankResult>, position: Int) {
        openDetailUi.value = detailUiData(gankPhotos, position)
    }


    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DetailDataActivity.REQUEST_DETAIL_DATA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                detailActivityReener.value = data
            } else {
                //错误信息
            }
        }
    }

    data class detailUiData(val gankPhotos: List<GankResult>, val position: Int)

}