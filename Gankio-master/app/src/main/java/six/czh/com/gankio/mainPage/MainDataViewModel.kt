package six.czh.com.gankio.mainPage

import android.app.Application
import android.arch.lifecycle.ViewModel
import six.czh.com.gankio.data.source.GankDataRepository

/**
 * Created by six.cai on 18-11-12.
 * Email: baicai94@foxmail.com
 */
class MainDataViewModel(
        private val context: Application,
        private val repository: GankDataRepository): ViewModel() {
}