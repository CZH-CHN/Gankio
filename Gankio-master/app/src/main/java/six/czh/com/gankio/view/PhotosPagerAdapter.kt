package six.czh.com.gankio.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.ImageViewTarget
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.detailData.DetailDataViewModel
import six.czh.com.gankio.detailData.detailDataContract
import six.czh.com.gankio.detailData.detailDataFragment
import six.czh.com.gankio.util.LogUtils
import six.czh.com.gankio.util.PAGE
import six.czh.com.gankio.util.PrefUtils
import java.lang.Exception

/**
 * Created by czh on 18-9-26.
 * Email: six.cai@czh.com
 */
//class PhotosPagerAdapter(var mPhotosList: List<GankResult>, val presenter: detailDataContract.Presenter): PagerAdapter() {
//    override fun isViewFromObject(p0: View, p1: Any): Boolean {
//        return p0 === p1 as View
//    }
//
//    override fun getCount(): Int = if(mPhotosList.isEmpty()) 0 else mPhotosList.size
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//
//        LogUtils.d("instantiateItem", "childCount = " + (mPhotosList.size / 10) + 1)
//        //需要加载更多
//        if (position == mPhotosList.size -1) {
//            //loadmore 告诉fragment
//
//        }
//
//        val view: View = initData(container, position)
//        container.addView(view)
//        return view
////        return super.instantiateItem(container, position)
//    }
//
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as View)
//    }
//
//    override fun getItemPosition(`object`: Any): Int {
//        return POSITION_NONE
//    }
//
//    private fun initData(container: ViewGroup, position: Int): View {
//        val gankResult = mPhotosList[position]
//        val layoutInflater = LayoutInflater.from(container.context)
//        val view: View = layoutInflater.inflate(R.layout.page_photo, container, false)
//
//
//        val photoView = view.findViewById<ImageView>(R.id.page_image)
//
//        val progressBar = view.findViewById<ProgressBar>(R.id.page_progress)
//
//        setPhoto(container.context, gankResult, progressBar, photoView, position)
//
//        return view
//    }
//
//    private fun setPhoto(context: Context, gankResult: GankResult, progressBar: ProgressBar,
//                         photoView: ImageView, position: Int) {
//        Glide.with(context).load(gankResult.url).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
//                .into(object : ImageViewTarget<Bitmap>(photoView) {
//                    override fun onLoadStarted(placeholder: Drawable?) {
//                        progressBar.visibility = View.VISIBLE
//                        super.onLoadStarted(placeholder)
//                    }
//
//                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
//                        progressBar.visibility = View.GONE
//                        super.onLoadFailed(e, errorDrawable)
//                    }
//
//                    override fun setResource(resource: Bitmap?) {
//                        progressBar.visibility = View.GONE
//                        photoView.setImageBitmap(resource)
//                        LogUtils.d("width = " + photoView.width + " height = " + photoView.height)
//                    }
//                })
//    }
//
//}