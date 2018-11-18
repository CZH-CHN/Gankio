package six.czh.com.gankio

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import six.czh.com.gankio.data.source.GankDataRepository
import six.czh.com.gankio.data.source.local.GankDataLocalSource
import six.czh.com.gankio.data.source.local.GankResultDatabase
import six.czh.com.gankio.data.source.remote.GankDataRemoteSource
import six.czh.com.gankio.detailData.DetailDataViewModel
import six.czh.com.gankio.loadAllData.LoadAllDataViewModel
import six.czh.com.gankio.mainPage.MainDataViewModel
import six.czh.com.gankio.util.AppExecutors

@Suppress("UNCHECKED_CAST")
/**
 * Created by czh on 18-11-1.
 * Email: baicai94@foxmail.com
 */
class ViewModelFactory private constructor(
        private val context: Application,
        private val repository: GankDataRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =

        with(modelClass) {
            when {
                isAssignableFrom(LoadAllDataViewModel::class.java) ->
                        LoadAllDataViewModel(context, repository)
                isAssignableFrom(DetailDataViewModel::class.java) ->
                        DetailDataViewModel(context, repository)
                isAssignableFrom(MainDataViewModel::class.java) ->
                    MainDataViewModel(context, repository)

                else ->
                    throw IllegalAccessException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T


    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application,
                            GankDataRepository.getInstance
                            (GankDataRemoteSource.getInstance(), GankDataLocalSource.getInstance(AppExecutors(),
                                    GankResultDatabase.getInstance(GankApplication.mContext).gankDataDao())))
                            .also { INSTANCE = it }
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}