package six.czh.com.gankio.loadAllData

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_empty.view.*
import kotlinx.android.synthetic.main.item_footer.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.loadAllData.scroll.OnLoadMoreListener
import six.czh.com.gankio.testAdapter.ItemViewBinder

/**
 * Created by cai on 18-12-10.
 * Email: baicai94@foxmail.com
 */
class EmptyVieWBinder(val callback: OnLoadMoreListener): ItemViewBinder<String, EmptyVieWBinder.ViewHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: String) {
        holder.itemView.item_empty_text_view.setOnClickListener {
            callback.onLoadMore()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.item_empty
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}