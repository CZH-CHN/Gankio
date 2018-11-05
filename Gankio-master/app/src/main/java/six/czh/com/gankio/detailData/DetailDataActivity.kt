package six.czh.com.gankio.detailData

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.frag_browse.*
import six.czh.com.gankio.R
import six.czh.com.gankio.util.ActivityUtils

class DetailDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_browse)

        setSupportActionBar(findViewById(R.id.toolbar))

        var fragment = supportFragmentManager.findFragmentById(R.id.browseFrame)

        if(fragment == null){
            fragment = DetailDataFragment()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, fragment, R.id.browseFrame)
        }

    }

    companion object {
        const val REQUEST_DETAIL_DATA = 1
        const val CURRENT_ITEM = "extra_current_item"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        //TODO 不符合MVP架构
        var intent = Intent()
        intent.putExtra(DetailDataActivity.CURRENT_ITEM, browse_viewpager.currentItem)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

}