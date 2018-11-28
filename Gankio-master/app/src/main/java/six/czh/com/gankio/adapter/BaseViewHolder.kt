package six.czh.com.gankio.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



/**
 * Created by six.cai on 18-11-27.
 * Email: baicai94@foxmail.com six.cai@six.cai.com
 */
class BaseViewHolder private constructor(private val itemView: View): RecyclerView.ViewHolder(itemView) {

    private var mViews: SparseArray<View> = SparseArray()

    companion object {
        fun get(context: Context, layoutid: Int, parent: ViewGroup):RecyclerView.ViewHolder {
            val view = LayoutInflater.from(context).inflate(layoutid, parent, false)

            return BaseViewHolder(view)
        }
    }

}