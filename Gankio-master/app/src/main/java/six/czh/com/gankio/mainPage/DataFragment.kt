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
import android.widget.Toast
import kotlinx.android.synthetic.main.item_container.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.ViewModelFactory
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.loadAllData.scroll.LoadMoreScrollListener
import six.czh.com.gankio.loadAllData.scroll.OnLoadMoreListener
import six.czh.com.gankio.util.PAGE
import six.czh.com.gankio.util.PrefUtils
import java.lang.IllegalArgumentException
import java.util.ArrayList

/**
 * Created by six.cai on 18-11-12.
 * Email: baicai94@foxmail.com
 */
class DataFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        var currentpage = (mDataList.size / 10) + 1
//        PrefUtils.applyInt(context, PAGE, currentpage)
        Log.d("czh", "refresh currentPage = $currentpage")

        when(type) {
            "Android" -> mViewModel.getAndroidData(10, 1)
            "iOS" -> mViewModel.getIosData(10,1)

            else -> throw IllegalArgumentException("check the data type is legal")
        }
    }

    private lateinit var mViewModel: MainDataViewModel

    lateinit var mRefreshLayout: SwipeRefreshLayout

    lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: DataAdapter

    private var mDataList = ArrayList<GankResult>()

    private lateinit var mScrollListener: LoadMoreScrollListener

    private var type = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.frag_main, container, false)

        mAdapter = DataAdapter()

        val layoutManager = LinearLayoutManager(context)

        mRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.main_refresh).apply {
            setOnRefreshListener(this@DataFragment)
        }

        mScrollListener = LoadMoreScrollListener().apply {
            setLayoutManager(layoutManager)
            setOnLoadMoreListener(object : OnLoadMoreListener {
                override fun onLoadMore() {
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

                            mAdapter.replaceData(it)
                            Log.v("six.cai", this@DataFragment.toString() + "   " + it.size)
                        }
                    })
                    mRefreshLayout.isRefreshing = false
                } else {
                    obtainViewModel().iosResults.observe(it, Observer {
                        if (it != null && it.isNotEmpty() && it[0].type.equals("iOS")) {

                            mAdapter.replaceData(it!!)
                            Log.v("six.cai1111", this@DataFragment.toString() + "   " + it!!.size)
                        }
                        mRefreshLayout.isRefreshing = false
                    })
                }


            }
        }

//        mViewModel.getGankData(arguments!!.getString("EXTRA_TYPE"), 10, 1)

        onRefresh()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        mRefreshLayout.isRefreshing = false
    }


    private fun obtainViewModel(): MainDataViewModel = ViewModelProviders
            .of(activity!!, ViewModelFactory.getInstance(application = activity!!.application))
            .get(MainDataViewModel::class.java)

    private inner class DataAdapter: RecyclerView.Adapter<MainViewHolder>() {

        fun replaceData(GankiodataList: List<GankResult>) {
            setList(GankiodataList)
            notifyDataSetChanged()
        }

        private fun setList(GankiodataList: List<GankResult>) {
            if (mDataList.isEmpty()) {
                mDataList = ArrayList(GankiodataList)
            } else {
                mDataList.clear()
                mDataList.addAll(GankiodataList)
            }


        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MainViewHolder {
            val view = LayoutInflater.from(p0.context).inflate(R.layout.item_container, p0, false)
            return MainViewHolder(view)
        }

        override fun getItemCount() = mDataList.size

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.bind(holder, mDataList.get(position))
        }

    }

    private inner class MainViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind(holder: MainViewHolder, data: GankResult) {

            with(view.item_container_tv) {
                text = data.desc
                setOnClickListener {
//                    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

