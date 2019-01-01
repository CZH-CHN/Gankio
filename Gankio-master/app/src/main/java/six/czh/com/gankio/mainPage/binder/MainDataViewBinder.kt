package six.czh.com.gankio.mainPage.binder

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_container.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.testAdapter.ItemViewBinder

/**
 * Created by cai on 18-12-21.
 * Email: baicai94@foxmail.com
 */
class MainDataViewBinder: ItemViewBinder<GankResult, MainDataViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return MainDataViewBinder.ViewHolder(inflater.inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: GankResult) {
        with(holder.itemView.item_container_tv) {
            text = item.desc
            setOnClickListener {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.item_with_no_image

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}