package six.czh.com.gankio

import android.app.Activity
import android.os.Bundle
import six.czh.com.myapplication.BasePresenter

/**
 * Created by Administrator on 2018/8/25 0025.
 */
class BaseActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}