package six.czh.com.gankio.detailData

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.frag_browse.*
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.util.LogUtils
import six.czh.com.gankio.view.PhotosPagerAdapter

class detailDataFragment: Fragment() {

    private lateinit var mBitmap: Bitmap

    private lateinit var mGankPhotos: ArrayList<GankResult>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.frag_browse, container, false)

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

//获取图片真正的宽高
//        Glide.with(this)
//                .load(intent?.getStringExtra("url"))
//                .asBitmap()//强制Glide返回一个Bitmap对象
//                .into(object : SimpleTarget<Bitmap>() {
//
//                    override fun onResourceReady(bitmap: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
//                        width = bitmap?.width
//                        height = bitmap?.height
//                        LogUtils.d( "width " + width) //200px
//                        LogUtils.d( "height " + height) //200px
//                    }
//                })


//        Glide.with(view.context)
//                .load(intent?.getStringExtra("url"))
//                .placeholder(R.color.color_glide_placeholder)
//                .dontAnimate()
//                .into(browse_image)
    }


    //设置图片大小
}