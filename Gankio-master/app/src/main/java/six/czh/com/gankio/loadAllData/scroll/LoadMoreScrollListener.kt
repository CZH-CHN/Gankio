package six.czh.com.gankio.loadAllData.scroll

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log

/**
 * 1.当滑动到底部时，自动加载更多(使用onScroll监听器；)
 */
class LoadMoreScrollListener : RecyclerView.OnScrollListener() {

    private var mLayoutManager: RecyclerView.LayoutManager? = null

    private var mListener: OnLoadMoreListener? = null

    private var mItemCount: Int = 0

    var isLoading: Boolean = false
        set

    private var lastItemPosition: Int = 0

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        this.mListener = listener
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        this.mLayoutManager = layoutManager
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy <= 0) return

        mItemCount = mLayoutManager!!.itemCount;

        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // get maximum element within the list
            lastItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        }
        if (mLayoutManager is LinearLayoutManager) {
            lastItemPosition = (mLayoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        }
        Log.d("onScrolled", "$lastItemPosition mItemCount = $mItemCount")
        if (!isLoading && mItemCount <= lastItemPosition + 1) {
            if (mListener != null) {
                mListener!!.onLoadMore()
            }
            isLoading = true
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}