package six.czh.com.gankio.mainPage

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import six.czh.com.gankio.R
import six.czh.com.gankio.ViewModelFactory
import six.czh.com.gankio.adapter.CommonAdapter
import six.czh.com.gankio.adapter.MultiTypeAdapter
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.loadAllData.scroll.LoadMoreScrollListener
import six.czh.com.gankio.loadAllData.scroll.OnLoadMoreListener
import six.czh.com.gankio.mainPage.binder.MainDataViewBinder
import six.czh.com.gankio.view.BaseFragments
import java.lang.IllegalArgumentException
import java.util.ArrayList

/**
 * Created by six.cai on 18-11-12.
 * Email: baicai94@foxmail.com
 */
class DataFragment: BaseFragments(), SwipeRefreshLayout.OnRefreshListener  {

    private var isRefresh = false

    override fun onRefresh() {
        if (isRefresh) return
        isRefresh = true
        val currentpage = if (isLoadMore) {
            (mDataList.size / 10) + 1
        } else {
            1
        }

//        PrefUtils.applyInt(context, PAGE, currentpage)
        Log.d("czh", "refresh currentPage = $currentpage")

        mViewModel.getGankData(type, 10, currentpage)
    }

    //用于判断是否为首次进入此页面。如果是首次进入此页面则只会更新最新的数据，而不会加载更多
    private var isLoadMore = false

    private lateinit var mViewModel: MainDataViewModel

    lateinit var mRefreshLayout: SwipeRefreshLayout

    lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: six.czh.com.gankio.testAdapter.MultiTypeAdapter

    private var mDataList = ArrayList<GankResult>()

    private lateinit var mScrollListener: LoadMoreScrollListener

    private var type = ""

    override fun onFragmentVisibleChange(isVisible: Boolean) {
        if (isVisible) {
            Toast.makeText(activity, "可见了", Toast.LENGTH_SHORT).show()
            onRefresh()
        } else {
            Toast.makeText(activity, "不可见了", Toast.LENGTH_SHORT).show()
        }
        super.onFragmentVisibleChange(isVisible)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.frag_main, container, false)

        mAdapter = six.czh.com.gankio.testAdapter.MultiTypeAdapter()

        mAdapter.register(
                GankResult::class.java,
                MainDataViewBinder())

        val layoutManager = LinearLayoutManager(context)

        mRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.main_refresh).apply {
            setOnRefreshListener(this@DataFragment)
        }

        mScrollListener = LoadMoreScrollListener().apply {
            setLayoutManager(layoutManager)
            setOnLoadMoreListener(object : OnLoadMoreListener {
                override fun onLoadMore() {
                    isLoadMore = true
                    onRefresh()
                }
            })
        }



        mRecyclerView = root.findViewById<RecyclerView>(R.id.main_recycler).apply {
            adapter = mAdapter
            this.layoutManager = layoutManager
            addOnScrollListener(mScrollListener)

        }


        type = arguments!!.getString("EXTRA_TYPE")
        mViewModel = obtainViewModel().apply {
            activity?.let {
                obtainViewModel().gankResults.observe(it, Observer {
                    isRefresh = false
                        if (it != null && it.isNotEmpty() /*&& it[0].type.equals("Android")*/) {
                            mDataList.clear()
                            mDataList.addAll(it)
                            mAdapter.items = mDataList.toList()
                            mAdapter.notifyDataSetChanged()
                        }
                        mRefreshLayout.isRefreshing = false
                        mScrollListener.isLoading = false
                        isLoadMore = false
                })

                obtainViewModel().errorMessage.observe(this@DataFragment.activity!!, Observer {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                })
            }
        }

        rootView = root
        return root
    }

    override fun onStop() {
        super.onStop()
        mRefreshLayout.isRefreshing = false
    }


    private fun obtainViewModel(): MainDataViewModel = ViewModelProviders
            .of(activity!!, ViewModelFactory.getInstance(application = activity!!.application))
            .get(MainDataViewModel::class.java)

}

