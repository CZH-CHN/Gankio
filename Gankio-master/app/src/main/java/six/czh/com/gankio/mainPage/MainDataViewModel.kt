package six.czh.com.gankio.mainPage

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import six.czh.com.gankio.R
import six.czh.com.gankio.SingleLiveEvent
import six.czh.com.gankio.UrlParams
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.source.GankDataRepository
import six.czh.com.gankio.data.source.GankDataSource
import six.czh.com.gankio.data.source.LOAD_DATA_IS_EMPTY
import six.czh.com.gankio.data.source.LOAD_NETWORK_ERROR

/**
 * Created by six.cai on 18-11-12.
 * Email: baicai94@foxmail.com
 */
class MainDataViewModel(
        private val context: Application,
        private val repository: GankDataRepository): ViewModel() {

//    var androidResults = MutableLiveData<List<GankResult>>()

    private val paramsInput = MutableLiveData<UrlParams>()

    val errorMessage = SingleLiveEvent<String>()

    var iosResults: LiveData<List<GankResult>> = Transformations.switchMap(paramsInput) {
        repository.getGankData(it.topic, it.num, it.page,  object : GankDataSource.LoadGankDataCallback {
            override fun onGankDataLoaded(gankResultList: GankData?) {
            }

            override fun onGankDataLoadedFail(errorCode: Int) {
                setErrorMessage(errorCode)
            }

        })
    }
    fun getIosData(num : Int, page : Int) {
        paramsInput.value = UrlParams("iOS", num, page)
    }

    var androidResults: LiveData<List<GankResult>> = Transformations.switchMap(paramsInput) {
        repository.getGankData(it.topic, it.num, it.page,  object : GankDataSource.LoadGankDataCallback {
            override fun onGankDataLoaded(gankResultList: GankData?) {
            }

            override fun onGankDataLoadedFail(errorCode: Int) {
                setErrorMessage(errorCode)
            }

        })
    }
    fun getAndroidData(num : Int, page : Int) {
        paramsInput.value = UrlParams("Android", num, page)
    }

    fun setErrorMessage(errorCode: Int) {
        errorMessage.value =
                when (errorCode) {
                    LOAD_DATA_IS_EMPTY -> context.resources.getString(R.string.load_data_empty)
                    LOAD_NETWORK_ERROR -> context.resources.getString(R.string.download_network_error)
                    else -> return
                }
    }

//    fun getGankData(topic : String, num : Int, page : Int) {
//        repository.getGankData(topic, num, page, object : GankDataSource.LoadGankDataCallback {
//            override fun onGankDataLoaded(gankResultList: GankData?) {
//
//                for (data: GankResult in gankResultList!!.results) {
//                    if (data.type.equals("Android")) {
//                        androidResults.value = gankResultList.results
//                    } else {
//                        iosResults.value = gankResultList.results
//                    }
//                }
//
//            }
//
//            override fun onGankDataLoadedFail(errorCode: Int) {
//
//            }
//
//        })
//    }



}