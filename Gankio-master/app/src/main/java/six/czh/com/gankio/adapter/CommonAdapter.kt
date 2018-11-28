package six.czh.com.gankio.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by six.cai on 18-11-26.
 * Email: baicai94@foxmail.com six.cai@six.cai.com
 */
abstract class CommonAdapter<T>(private var mLayoutId: Int, var mDatas: ArrayList<T>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(layoutId: Int) : this(layoutId, ArrayList<T>()) {
        mLayoutId = layoutId
    }

    //更新数据的接口
    fun replaceData(DataList: List<T>) {
        setList(DataList)
        notifyDataSetChanged()
    }

    private fun setList(DataList: List<T>) {
        if (mDatas.isEmpty()) {
            mDatas = ArrayList(DataList)
        } else {
            mDatas.clear()
            mDatas.addAll(DataList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = BaseViewHolder.get(parent.context, mLayoutId, parent)

        return viewHolder
    }

    override fun getItemCount() = mDatas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        convert(holder, mDatas[position], position)
    }

    //抽象方法
    abstract fun convert (holder: RecyclerView.ViewHolder, t: T, position: Int)
}