package six.czh.com.gankio.mainPage.binder

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_container.view.*
import kotlinx.android.synthetic.main.item_with_three_image.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.testAdapter.ItemViewBinder

/**
 * Created by cai on 18-12-21.
 * Email: baicai94@foxmail.com
 */
class ThreeImageViewBinder: ItemViewBinder<GankResult, ThreeImageViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ThreeImageViewBinder.ViewHolder(inflater.inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: GankResult) {
        Log.d("six.cai.binder", "" + item.toString())
        with(holder.itemView.item_three_tv) {
            text = item.desc
            setOnClickListener {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        }
        with(holder.itemView.item_three_iv1) {
            Glide.with(holder.itemView.context)
                    .load(item.images[0])
                    .placeholder(R.color.color_glide_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .override(400, 600)
                    .into(this)
        }

        with(holder.itemView.item_three_iv2) {
            Glide.with(holder.itemView.context)
                    .load(item.images[1])
                    .placeholder(R.color.color_glide_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .override(400, 600)
                    .into(this)
        }

        if (item.images.size == 2) {
            holder.itemView.item_three_iv3.visibility = View.GONE
        } else {
            holder.itemView.item_three_iv3.visibility = View.VISIBLE
            with(holder.itemView.item_three_iv3) {
                Glide.with(holder.itemView.context)
                        .load(item.images[2])
                        .placeholder(R.color.color_glide_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .override(400, 600)
                        .into(this)
            }
        }

    }

    override fun getLayoutId(): Int = R.layout.item_with_three_image

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}