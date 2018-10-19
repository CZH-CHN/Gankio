package six.czh.com.gankio.detailData

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_browse.*
import kotlinx.android.synthetic.main.frag_browse.*
import six.czh.com.gankio.R
import six.czh.com.gankio.util.ActivityUtils

class detailDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_browse)

        setSupportActionBar(findViewById(R.id.toolbar))
//        setupActionBar(R.id.toolbar) {
//            setHomeAsUpIndicator(R.drawable.ic_launcher_background)
//            setDisplayHomeAsUpEnabled(true)
//        }


        var fragment = supportFragmentManager.findFragmentById(R.id.browseFrame)

        if(fragment == null){
            fragment = detailDataFragment()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, fragment, R.id.browseFrame)
        }

    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.detaildata_fragment_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            R.id.menu_share -> Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show()
//            R.id.menu_save -> Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show()
//        }
//        return true
//    }

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
        intent.putExtra(detailDataActivity.CURRENT_ITEM, browse_viewpager.currentItem)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

//    fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
//        setSupportActionBar(findViewById(toolbarId))
//        supportActionBar?.run {
//            action()
//        }
//    }


}