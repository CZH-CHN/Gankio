package six.czh.com.gankio.loadAllData

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_main.view.*
import six.czh.com.gankio.GankApplication
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.data.source.GankDataRepository
import six.czh.com.gankio.data.source.local.GankDataLocalSource
import six.czh.com.gankio.data.source.local.GankResultDatabase
import six.czh.com.gankio.data.source.remote.GankDataRemoteSource
import six.czh.com.gankio.detailData.detailDataActivity
import six.czh.com.gankio.loadAllData.loadAlldataFragment.DataAdapter.MainViewHolder
import six.czh.com.gankio.loadAllData.scroll.OnLoadMoreListener
import six.czh.com.gankio.loadAllData.scroll.LoadMoreScrollListener
import six.czh.com.gankio.util.AppExecutors
import six.czh.com.gankio.util.LogUtils
import six.czh.com.myapplication.loadAllData.LoadAlldataContract
import java.util.ArrayList

/**
 * Created by Administrator on 2018/8/25 0025.
 */
//全局变量page
var page = 1

class loadAlldataFragment : Fragment(), LoadAlldataContract.View, SwipeRefreshLayout.OnRefreshListener {

    private var listSize = 0

    override fun showAllData(data: Intent?) {
        LogUtils.d("获取返回值")
        if (data != null) {
            mRecyclerView.scrollToPosition(data.getIntExtra(detailDataActivity.CURRENT_ITEM, 0))
        }


    }

    internal var itemListener: DataItemListener = object : DataItemListener {
        override fun onDataItemClick(gankPhotos: List<GankResult>, position: Int) {
            presenter.openDataDetails(gankPhotos, position)
        }

    }

    override fun showDetailUi(gankPhotos: List<GankResult>, position: Int) {
        val intent = Intent()
        intent.setClass(context, detailDataActivity::class.java)
        intent.putParcelableArrayListExtra("gankPhotos", gankPhotos as ArrayList<Parcelable>)
        intent.putExtra("position", position)
        startActivityForResult(intent, detailDataActivity.REQUEST_DETAIL_DATA)
    }

    private lateinit var mScrollListener: LoadMoreScrollListener

    //加载后续数据
    override fun onRefresh() {
        var currentpage = (listSize / 10) + 1
        Log.d("czh", "refresh currentPage = $currentpage")
//        mRefreshLayout.isRefreshing = true
//        if(page <= 0) {
//            page = 1
//        }
        presenter.loadMsg("福利", 10, currentpage)
    }

    override lateinit var presenter: LoadAlldataContract.Presenter

    lateinit var mRefreshLayout: SwipeRefreshLayout

    lateinit var mRecyclerView: RecyclerView

    override fun loadMsgSuccess(gankResultList: List<GankResult>) {
//        ++page
        listSize = gankResultList.size
        mRefreshLayout.isRefreshing = false
        mScrollListener.isLoading = false
        mAdapter.replaceData(gankResultList)
    }

    override fun loadMsgFail() {
//        --page
        mRefreshLayout.isRefreshing = false
        mScrollListener.isLoading = false
        Toast.makeText(activity, "请检查网络", Toast.LENGTH_LONG).show()
    }

    private lateinit var mAdapter: DataAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.frag_main, container, false)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        mScrollListener = LoadMoreScrollListener().apply {
            setLayoutManager(layoutManager)
            setOnLoadMoreListener(object : OnLoadMoreListener {
                override fun onLoadMore() {
                    onRefresh()
                }
            })
        }

        mAdapter = DataAdapter(ArrayList<GankResult>(0), itemListener)

        mRecyclerView = root.findViewById<RecyclerView>(R.id.main_recycler).apply {
            adapter = mAdapter
            this.layoutManager = layoutManager
            addOnScrollListener(mScrollListener)

        }



        mRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.main_refresh).apply {
            setOnRefreshListener(this@loadAlldataFragment)
        }
        onRefresh()
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LoadAlldataPresenter(GankDataRepository.getInstance(GankDataRemoteSource.getInstance(), GankDataLocalSource.getInstance(AppExecutors(),GankResultDatabase.getInstance(GankApplication.mContext).gankDataDao())),this@loadAlldataFragment)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.result(requestCode, resultCode, data)
    }

    private class DataAdapter(var DataList: ArrayList<GankResult>, val listener: DataItemListener) : RecyclerView.Adapter<MainViewHolder>() {


        override fun getItemCount(): Int = DataList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val istrurl = DataList.get(position).url
            if (TextUtils.isEmpty(istrurl)) {
                return
            }
            holder.bind(holder, DataList.get(position), listener)

            holder.view.item_main_iv.setOnClickListener {
                listener.onDataItemClick(DataList, position)
            }
        }

        fun replaceData(GankiodataList: List<GankResult>) {
            setList(GankiodataList)
            notifyDataSetChanged()
        }

        private fun setList(GankiodataList: List<GankResult>) {
            if (DataList.isEmpty()) {
                DataList = ArrayList(GankiodataList)
            } else {
                DataList.clear()
                DataList.addAll(GankiodataList)
            }

            Log.d("ccccc", "" + DataList.size)

        }

        private class MainViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

            fun bind(holder: MainViewHolder, data: GankResult, listener: DataItemListener) {

                with(view.item_main_iv) {
                    Glide.with(view.context)
                            .load(data.url)
                            .placeholder(R.color.color_glide_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(400, 600)
                            .into(this)
//                    setOnClickListener {
//                        listener.onDataItemClick(data)
//                    }
                }
            }
        }
    }

    interface DataItemListener {
        fun onDataItemClick(DataList: List<GankResult>, position: Int)
    }
}

