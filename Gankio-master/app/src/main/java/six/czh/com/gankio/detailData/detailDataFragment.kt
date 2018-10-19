package six.czh.com.gankio.detailData

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.frag_browse.*
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.util.LogUtils
import six.czh.com.gankio.view.PhotosPagerAdapter

class detailDataFragment: Fragment(), detailDataContract.View {

    override lateinit var presenter: detailDataContract.Presenter

    override fun loadImageSuccess() {
    }

    override fun loadImageFail() {
    }

    /**
     * 返回到首页
     */
    override fun showAllData() {
        activity?.apply {
            var intent = Intent()
            intent.putExtra(detailDataActivity.CURRENT_ITEM, browse_viewpager.currentItem)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private lateinit var mBitmap: Bitmap

    private lateinit var mGankPhotos: ArrayList<GankResult>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.frag_browse, container, false)

        //TODO ：需要总结此项
        setHasOptionsMenu(true)
        return root
    }


    var width: Int? = 1
    var height: Int? = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = activity?.intent

        mGankPhotos = intent!!.extras.getParcelableArrayList("gankPhotos")

        with(browse_viewpager) {
            adapter = PhotosPagerAdapter(mGankPhotos)
            currentItem = intent.extras.getInt("position")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.detaildata_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_share -> Toast.makeText(context, "分享", Toast.LENGTH_SHORT).show()
            R.id.menu_save -> Toast.makeText(context, "保存", Toast.LENGTH_SHORT).show()
        }
        return true
    }


//    Intent shareIntent = new Intent();
//    shareIntent.setType("image/*");
//    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    shareIntent.setAction(Intent.ACTION_SEND);
//    shareIntent.putExtra(Intent.EXTRA_STREAM,
//    MediaUtil.getInstace().getImageContentUri(BrowseActivity.this, photoFile));
//    startActivity(Intent.createChooser(shareIntent,
//    getString(R.string.share)));
    //设置图片大小
}