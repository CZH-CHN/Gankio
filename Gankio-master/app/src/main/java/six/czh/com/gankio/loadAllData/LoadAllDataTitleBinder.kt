package six.czh.com.gankio.loadAllData

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_container.view.*
import kotlinx.android.synthetic.main.item_footer.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankData
import six.czh.com.gankio.loadAllData.scroll.OnLoadMoreListener
import six.czh.com.gankio.testAdapter.ItemViewBinder

/**
 * Created by cai on 18-12-7.
 * Email: baicai94@foxmail.com
 */
class LoadAllDataTitleBinder(val callback: OnLoadMoreListener): ItemViewBinder<LoadAllDataTitleBean, LoadAllDataTitleBinder.ViewHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): LoadAllDataTitleBinder.ViewHolder {

        return LoadAllDataTitleBinder.ViewHolder(inflater.inflate(layoutId, parent, false), callback)
    }

    override fun onBindViewHolder(holder: LoadAllDataTitleBinder.ViewHolder, item: LoadAllDataTitleBean) {
        holder.setData(item)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_footer
    }

    class ViewHolder(itemView: View, private val callback: OnLoadMoreListener): RecyclerView.ViewHolder(itemView) {
        fun setData(@NonNull item: LoadAllDataTitleBean) {

            if (item.status == 1) {
                itemView.item_footer_progress_bar.visibility = View.GONE
                itemView.item_footer_button.visibility = View.VISIBLE
                itemView.item_footer_button.text = item.title

                itemView.item_footer_button.setOnClickListener {
                    itemView.item_footer_button.visibility = View.GONE
                    itemView.item_footer_progress_bar.visibility = View.VISIBLE
                    Log.d("LoadAllDataTitleBinder", "刷新")
                    callback.onLoadMore()
                }


            } else {
                itemView.item_footer_button.visibility = View.GONE
                itemView.item_footer_progress_bar.visibility = View.VISIBLE
//                itemView.item_footer_button.text = item.title
            }
        }
    }




}