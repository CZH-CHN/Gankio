package six.czh.com.gankio.loadAllData

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_container.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.testAdapter.ItemViewBinder

/**
 * Created by oneplus on 18-12-7.
 * Email: six.cai@oneplus.com
 */
class LoadAllDataTitleBinder: ItemViewBinder<GankResult, LoadAllDataBinder.ViewHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): LoadAllDataBinder.ViewHolder {

        return LoadAllDataBinder.ViewHolder(inflater.inflate(R.layout.item_container, parent, false))
    }

    override fun onBindViewHolder(holder: LoadAllDataBinder.ViewHolder, item: GankResult) {

        holder.itemView.item_container_tv.text = item.publishedAt
    }

    override fun getLayoutId(): Int {
        return R.layout.item_container
    }
}