package six.czh.com.gankio.detailData

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.frag_browse.*
import kotlinx.android.synthetic.main.page_photo.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.download.*
import six.czh.com.gankio.util.AppExecutors
import six.czh.com.gankio.util.LogUtils
import six.czh.com.gankio.util.MediaUtils
import six.czh.com.gankio.view.PhotosPagerAdapter
import java.io.File

class detailDataFragment: Fragment(), detailDataContract.View {

    //打开分享页面
    override fun startShareActivity(file: File) {
        val shareIntent = Intent()
        shareIntent.type = "image/*"
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM,
                MediaUtils.getInstance().getImageContentUri(context, file))
        startActivity(Intent.createChooser(shareIntent,
                getString(R.string.menu_action_share)))
    }

    override fun showSaveImageSuccess(file: File) {
        Toast.makeText(context, resources.getString(R.string.download_success), Toast.LENGTH_SHORT).show()

        browse_viewpager.page_progress.visibility = View.GONE
        //TODO 媒体库更新方法需要修改
        context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
    }

    //保存图片失败
    override fun showSaveImageFailed(errorCode: Int) {
        var toast = ""
        when (errorCode) {
            DOWNLOAD_DIR_NO_EXISTED -> toast = resources.getString(R.string.download_fail)
            DOWNLOAD_PERMISSION_DENIED -> toast = resources.getString(R.string.download_fail_permission)
            DOWNLOAD_FILE_IS_EXISTS -> toast = resources.getString(R.string.download_file_exist)
            DOWNLOAD_NETWORK_ERROR -> toast = resources.getString(R.string.download_network_error)
            DOWNLOAD_WRITE_FILE_ERROR -> toast = resources.getString(R.string.download_fail)

        }
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
        browse_viewpager.page_progress.visibility = View.GONE
    }


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = DetailDataPresenter(GankDataDownload(AppExecutors()), this@detailDataFragment)
    }

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
            R.id.menu_share -> {
                presenter.shareImage(mGankPhotos[browse_viewpager.currentItem].url)
            }
            R.id.menu_save ->  {
                presenter.saveImage(mGankPhotos[browse_viewpager.currentItem].url)
                browse_viewpager.page_progress.visibility = View.VISIBLE
            }

        }
        return true
    }


}