package six.czh.com.gankio.loadAllData

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import six.czh.com.gankio.R
import six.czh.com.gankio.util.ActivityUtils

/**
 * Created by Administrator on 2018/8/25 0025.
 */
class LoadAlldataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fragment = supportFragmentManager.findFragmentById(R.id.contentFrame)

        if(fragment == null){
            fragment = loadAlldataFragment()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, fragment, R.id.contentFrame)
        }
    }
}