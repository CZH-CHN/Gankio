package six.czh.com.gankio.loadAllData

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_main.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.ViewModelFactory
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.detailData.detailDataActivity
import six.czh.com.gankio.loadAllData.loadAlldataFragment.DataAdapter.MainViewHolder
import six.czh.com.gankio.loadAllData.scroll.OnLoadMoreListener
import six.czh.com.gankio.loadAllData.scroll.LoadMoreScrollListener
import six.czh.com.gankio.util.LogUtils
import six.czh.com.gankio.util.PAGE
import six.czh.com.gankio.util.PrefUtils
import java.util.ArrayList

/**
 * Created by Administrator on 2018/8/25 0025.
 */
//全局变量page, 每次进入时都读取值
var page = 1

class loadAlldataFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var listSize = 0

    private var currentPosition = 0

    private lateinit var mScrollListener: LoadMoreScrollListener

    //加载后续数据
    override fun onRefresh() {
        var currentpage = (listSize / 10) + 1
        PrefUtils.applyInt(context, PAGE, currentpage)
        Log.d("czh", "refresh currentPage = $currentpage")

        gankDataViewModel.getGankData("福利", 10, currentpage)

        gankDataViewModel.gankResults.observe(this@loadAlldataFragment.activity!!, Observer<List<GankResult>> {
            if (it != null) {
                Log.d("czh", "data.size = " + it.size)
                listSize = it.size
                mRefreshLayout.isRefreshing = false
                mScrollListener.isLoading = false
                mAdapter.replaceData(it)
                if (currentPosition != 0) {
                    mRecyclerView.scrollToPosition(currentPosition)
                    currentPosition = 0
                }
            }

        })

    }

//    private fun openDetailUi(gankPhotos: List<GankResult>, position: Int) {
//        obtainViewModel().openDetailUi.observe(this@loadAlldataFragment.activity!!, Observer {
//            val intent = Intent()
//            intent.setClass(context, detailDataActivity::class.java)
//            intent.putParcelableArrayListExtra("gankPhotos", gankPhotos as ArrayList<Parcelable>)
//            intent.putExtra("position", position)
//            startActivityForResult(intent, detailDataActivity.REQUEST_DETAIL_DATA)
//        })
//    }


    private fun obtainViewModel(): LoadAllDataViewModel = ViewModelProviders
            .of(activity!!, ViewModelFactory.getInstance(application = activity!!.application))
            .get(LoadAllDataViewModel::class.java)

    lateinit var gankDataViewModel: LoadAllDataViewModel

    lateinit var mRefreshLayout: SwipeRefreshLayout

    lateinit var mRecyclerView: RecyclerView

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

        mAdapter = DataAdapter(ArrayList<GankResult>(0), gankDataViewModel)

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
//        gankDataViewModel = ViewModelProviders.of(this).get(LoadAllDataViewModel::class.java)




        gankDataViewModel = obtainViewModel().apply {
            obtainViewModel().detailActivityReener.observe(this@loadAlldataFragment.activity!!, Observer {

                LogUtils.d("获取返回值" + it?.getIntExtra(detailDataActivity.CURRENT_ITEM, 0))
                LogUtils.d("获取返回值 当前item数量" + mRecyclerView.adapter?.itemCount)
                currentPosition = it!!.getIntExtra(detailDataActivity.CURRENT_ITEM, 0)

                if (currentPosition < mRecyclerView.adapter!!.itemCount) {
                    mRecyclerView.scrollToPosition(currentPosition)
                }
            })


            obtainViewModel().openDetailUi.observe(this@loadAlldataFragment.activity!!, Observer {
                val intent = Intent()
                intent.setClass(context, detailDataActivity::class.java)
                intent.putParcelableArrayListExtra("gankPhotos", it?.gankPhotos as ArrayList<Parcelable>)
                intent.putExtra("position", it.position)
                startActivityForResult(intent, detailDataActivity.REQUEST_DETAIL_DATA)
            })



        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        obtainViewModel().handleActivityResult(requestCode, resultCode, data)
    }

    private inner class DataAdapter(var DataList: ArrayList<GankResult>, val loadAllDataViewModel: LoadAllDataViewModel) : RecyclerView.Adapter<MainViewHolder>() {


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
            holder.bind(holder, DataList.get(position))

            holder.view.item_main_iv.setOnClickListener {
//                openDetailUi(DataList, position)
                loadAllDataViewModel.openDetailUi(DataList, position)
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

        private inner class MainViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

            fun bind(holder: MainViewHolder, data: GankResult) {

                with(view.item_main_iv) {
                    Glide.with(view.context)
                            .load(data.url)
                            .placeholder(R.color.color_glide_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(400, 600)
                            .into(this)
                }
            }
        }
    }

}

