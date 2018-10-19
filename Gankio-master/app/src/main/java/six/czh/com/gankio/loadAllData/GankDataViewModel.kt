package six.czh.com.gankio.loadAllData

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.data.source.GankDataRepository
import six.czh.com.gankio.data.source.remote.GankDataRemoteSource

/**
 * Created by czh on 18-10-9.
 * Email: six.cai@czh.com
 */
class GankDataViewModel: ViewModel() {

    private var mGankDatas: LiveData<List<GankData>> = MutableLiveData()

    fun getGankData(topic : String, num : Int, page : Int): LiveData<List<GankData>> {

        return mGankDatas
    }


    private fun fetchGankData(topic : String, num : Int, page : Int) {
//        GankDataRepository.getInstance(GankDataRemoteSource.getInstance()).getGankData(topic, num, page)
    }
}