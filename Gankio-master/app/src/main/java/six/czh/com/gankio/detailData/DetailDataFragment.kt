package six.czh.com.gankio.detailData

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.ImageViewTarget
import kotlinx.android.synthetic.main.frag_browse.*
import kotlinx.android.synthetic.main.page_photo.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.ViewModelFactory
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.util.LogUtils
import six.czh.com.gankio.util.MediaUtils
import java.lang.Exception

const val ACTION_SHARE = 1
const val ACTION_SAVE = 2
class DetailDataFragment: Fragment(){

    var mCurrentAction = 0


    lateinit var viewModel: DetailDataViewModel


    fun showAllDataUi() {
        viewModel.showAllDataUi(browse_viewpager.currentItem)
    }

    private lateinit var mGankPhotos: ArrayList<GankResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(DetailDataViewModel::class.java)
        viewModel = obtainViewModel().apply {
            obtainViewModel().position.observe(this@DetailDataFragment.activity!!, Observer {
                activity?.apply {
                    var intent = Intent()
                    intent.putExtra(DetailDataActivity.CURRENT_ITEM, it)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            })

            obtainViewModel().toastMessage.observe(this@DetailDataFragment.activity!!, Observer {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })
            obtainViewModel().downloadResult.observe(this@DetailDataFragment.activity!!, Observer {

                when (it!!.type) {
                    1 -> {
                        val shareIntent = Intent()
                        shareIntent.type = "image/*"
                        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.putExtra(Intent.EXTRA_STREAM,
                                MediaUtils.getInstance().getImageContentUri(context, it!!.file))
                        startActivity(Intent.createChooser(shareIntent,
                                getString(R.string.menu_action_share)))
                    }
                    2 -> {
                        Toast.makeText(context, resources.getString(R.string.download_success), Toast.LENGTH_SHORT).show()

                        browse_viewpager.page_progress.visibility = View.GONE
                        //TODO 媒体库更新方法需要修改
                        context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(it?.file)))
                    }

                }

            })

        }
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
            adapter = PhotosPagerAdapter()
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
                        viewModel.downloadImage(mGankPhotos[browse_viewpager.currentItem].url, 1)
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
                        viewModel.downloadImage(mGankPhotos[browse_viewpager.currentItem].url, 2)
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
                    viewModel.downloadImage(mGankPhotos[browse_viewpager.currentItem].url, 1)
                } else if (mCurrentAction == ACTION_SAVE){
                    viewModel.downloadImage(mGankPhotos[browse_viewpager.currentItem].url, 2)
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

    private fun obtainViewModel(): DetailDataViewModel = ViewModelProviders
            .of(activity!!, ViewModelFactory.getInstance(application = activity!!.application))
            .get(DetailDataViewModel::class.java)


    private inner class PhotosPagerAdapter: PagerAdapter() {
        override fun isViewFromObject(p0: View, p1: Any): Boolean {
            return p0 === p1 as View
        }

        override fun getCount(): Int = if(mGankPhotos.isEmpty()) 0 else mGankPhotos.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            Toast.makeText(context, "" + position + "/" + mGankPhotos.size , Toast.LENGTH_SHORT).show()
            //需要加载更多
            if (position >= mGankPhotos.size - 2) {
                //loadmore 告诉fragment

                viewModel.getGankData("福利", 10 , (mGankPhotos.size / 10) + 1)
                viewModel.gankResults.observe(this@DetailDataFragment.activity!!, Observer<List<GankResult>> { it ->
                    if (it != null) {
                        mGankPhotos = it as ArrayList<GankResult>
                        notifyDataSetChanged()
                    }
                })

            }

            val view: View = initData(container, position)
            container.addView(view)
            return view
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }


        private fun initData(container: ViewGroup, position: Int): View {
            LogUtils.d("initData", "" + position)
            val gankResult = mGankPhotos[position]
            val layoutInflater = LayoutInflater.from(container.context)
            val view: View = layoutInflater.inflate(R.layout.page_photo, container, false)


            val photoView = view.findViewById<ImageView>(R.id.page_image)

            val progressBar = view.findViewById<ProgressBar>(R.id.page_progress)

            setPhoto(container.context, gankResult, progressBar, photoView, position)

            return view
        }

        private fun setPhoto(context: Context, gankResult: GankResult, progressBar: ProgressBar,
                             photoView: ImageView, position: Int) {
            Glide.with(context).load(gankResult.url).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                    .into(object : ImageViewTarget<Bitmap>(photoView) {
                        override fun onLoadStarted(placeholder: Drawable?) {
                            progressBar.visibility = View.VISIBLE
                            super.onLoadStarted(placeholder)
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            progressBar.visibility = View.GONE
                            super.onLoadFailed(e, errorDrawable)
                        }

                        override fun setResource(resource: Bitmap?) {
                            progressBar.visibility = View.GONE
                            photoView.setImageBitmap(resource)
                            LogUtils.d("width = " + photoView.width + " height = " + photoView.height)
                        }
                    })
        }

    }

}

