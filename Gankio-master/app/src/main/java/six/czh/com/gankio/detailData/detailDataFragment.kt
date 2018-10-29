package six.czh.com.gankio.detailData

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
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

const val ACTION_SHARE = 1
const val ACTION_SAVE = 2
class detailDataFragment: Fragment(), detailDataContract.View {

    var mCurrentAction = 0

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
        browse_viewpager.page_progress.visibility = View.GONE
        var toast = ""
        when (errorCode) {
            DOWNLOAD_DIR_NO_EXISTED -> toast = resources.getString(R.string.download_fail)
            DOWNLOAD_PERMISSION_DENIED -> toast = resources.getString(R.string.download_fail_permission)
            DOWNLOAD_FILE_IS_EXISTS -> toast = resources.getString(R.string.download_file_exist)
            DOWNLOAD_NETWORK_ERROR -> toast = resources.getString(R.string.download_network_error)
            DOWNLOAD_WRITE_FILE_ERROR -> toast = resources.getString(R.string.download_fail)

        }
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()

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
            adapter = PhotosPagerAdapter(mGankPhotos, presenter)
            currentItem = intent.extras.getInt("position")
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.detaildata_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_share -> {
                mCurrentAction = ACTION_SHARE
                //检查权限
                context?.let {
                    if (ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) run {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            requestPermissions(Array(1, {Manifest.permission.WRITE_EXTERNAL_STORAGE}), 10)
                        } else {
                            LogUtils.d("被直接拒绝了")
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            intent.data = Uri.fromParts("package", context?.packageName, null)
                            startActivity(intent)
                        }
                    } else {
                        presenter.shareImage(mGankPhotos[browse_viewpager.currentItem].url)
                    }
                }

            }
            R.id.menu_save ->  {
                mCurrentAction = ACTION_SAVE
                //检查权限
                context?.let {
                    if (ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) run {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            requestPermissions(Array(1, {Manifest.permission.WRITE_EXTERNAL_STORAGE}), 10)
                        } else {
                            LogUtils.d("被直接拒绝了")
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            intent.data = Uri.fromParts("package", context?.packageName, null)
                            startActivity(intent)
                        }

                    } else {
                        presenter.saveImage(mGankPhotos[browse_viewpager.currentItem].url)
                        browse_viewpager.page_progress.visibility = View.VISIBLE
                    }
                }

            }

        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {

            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获得权限了就跳转界面
                if (mCurrentAction == ACTION_SHARE) {
                    presenter.shareImage(mGankPhotos[browse_viewpager.currentItem].url)
                } else if (mCurrentAction == ACTION_SAVE){
                    presenter.saveImage(mGankPhotos[browse_viewpager.currentItem].url)
                    browse_viewpager.page_progress.visibility = View.VISIBLE
                }
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //当用户选择不授权，且没有点击 不在询问的选项
//                    showPermissionDialog(REQUEST_PERMISSION_DIALOG)
                } else {
                    //当用户选择不同意授权并且 选择了不再询问。
                    //跳转到设置界面
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.data = Uri.fromParts("package", context?.packageName, null)
                    startActivity(intent)
                }
            }

        }
    }

}