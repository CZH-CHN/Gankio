package six.czh.com.gankio.detailData

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import six.czh.com.gankio.R
import six.czh.com.gankio.util.ActivityUtils

class detailDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_browse)

        var fragment = supportFragmentManager.findFragmentById(R.id.browseFrame)

        if(fragment == null){
            fragment = detailDataFragment()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, fragment, R.id.browseFrame)
        }
    }

}