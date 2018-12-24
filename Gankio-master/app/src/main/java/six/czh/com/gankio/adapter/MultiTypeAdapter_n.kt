package six.czh.com.gankio.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by cai on 18-12-4.
 * Email: baicai94@foxmail.com
 */
abstract class MultiTypeAdapter<T>(private var multiItemTypeSupport: MultiItemTypeSupport<T>,
                                   mDatas: ArrayList<T>): CommonAdapter<T>(-1, mDatas) {

    constructor(multiItemTypeSupport: MultiItemTypeSupport<T>) : this(multiItemTypeSupport, ArrayList<T>()) {
    }

    override fun getItemViewType(position: Int): Int {
        return multiItemTypeSupport.getItemViewType(position, mDatas.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BaseViewHolder.get(parent.context, multiItemTypeSupport.getLayoutId(viewType), parent)
    }
}


interface MultiItemTypeSupport<T> {
    fun getLayoutId(itemType: Int): Int

    fun getItemViewType(position: Int, t: T): Int
}