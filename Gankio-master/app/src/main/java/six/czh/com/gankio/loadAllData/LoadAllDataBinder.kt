package six.czh.com.gankio.loadAllData

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.testAdapter.ItemViewBinder

/**
 * Created by oneplus on 18-12-7.
 * Email: six.cai@oneplus.com
 */
abstract class LoadAllDataBinder: ItemViewBinder<GankResult, LoadAllDataBinder.ViewHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {

        return ViewHolder(inflater.inflate(R.layout.item_main, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: GankResult) {
        convert(holder, item)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_main
    }

    //抽象方法
    abstract fun convert (holder: RecyclerView.ViewHolder, gankResult: GankResult)

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}