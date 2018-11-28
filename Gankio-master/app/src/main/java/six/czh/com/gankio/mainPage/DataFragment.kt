package six.czh.com.gankio.mainPage

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.item_container.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.ViewModelFactory
import six.czh.com.gankio.adapter.CommonAdapter
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.loadAllData.scroll.LoadMoreScrollListener
import six.czh.com.gankio.loadAllData.scroll.OnLoadMoreListener
import six.czh.com.gankio.util.PAGE
import six.czh.com.gankio.util.PrefUtils
import six.czh.com.gankio.view.BaseFragments
import java.lang.IllegalArgumentException
import java.util.ArrayList

/**
 * Created by six.cai on 18-11-12.
 * Email: baicai94@foxmail.com
 */
class DataFragment: BaseFragments(), SwipeRefreshLayout.OnRefreshListener  {
    override fun onRefresh() {
        val currentpage = if (isLoadMore) {
            (mDataList.size / 10) + 1
        } else {
            1
        }

//        PrefUtils.applyInt(context, PAGE, currentpage)
        Log.d("czh", "refresh currentPage = $currentpage")

        when(type) {
            "Android" -> mViewModel.getAndroidData(10, currentpage)
            "iOS" -> mViewModel.getIosData(10,currentpage)

            "all" -> mViewModel.getAndroidData(10, currentpage)
            else -> throw IllegalArgumentException("check the data type is legal")
        }
    }

    //用于判断是否为首次进入此页面。如果是首次进入此页面则只会更新最新的数据，而不会加载更多
    private var isLoadMore = false

    private lateinit var mViewModel: MainDataViewModel

    lateinit var mRefreshLayout: SwipeRefreshLayout

    lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: CommonAdapter<GankResult>

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

        mAdapter = object : CommonAdapter<GankResult>(R.layout.item_container, mDataList) {
            override fun convert(holder: RecyclerView.ViewHolder, t: GankResult, position: Int) {
                with(holder.itemView.item_container_tv) {
                    text = t.desc
                    setOnClickListener {
                        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

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
                if (type == "Android") {
                    obtainViewModel().androidResults.observe(it, Observer {
                        if (it != null && it.isNotEmpty() && it[0].type.equals("Android")) {
                            mDataList.clear()
                            mDataList.addAll(it)
                            mAdapter.replaceData(it)
                        }
                        mRefreshLayout.isRefreshing = false
                        mScrollListener.isLoading = false
                        isLoadMore = false
                    })

                } else {
                    obtainViewModel().iosResults.observe(it, Observer {
                        if (it != null && it.isNotEmpty() && it[0].type.equals("iOS")) {
                            mDataList.clear()
                            mDataList.addAll(it)
                            mAdapter.replaceData(it)
                        }
                        mRefreshLayout.isRefreshing = false
                        mScrollListener.isLoading = false
                        isLoadMore = false
                    })
                }

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

