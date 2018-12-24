package six.czh.com.gankio.loadAllData

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_main.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.ViewModelFactory
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.detailData.DetailDataActivity
import six.czh.com.gankio.loadAllData.scroll.OnLoadMoreListener
import six.czh.com.gankio.loadAllData.scroll.LoadMoreScrollListener
import six.czh.com.gankio.util.PAGE
import six.czh.com.gankio.util.PrefUtils
import six.czh.com.gankio.view.BaseFragments
import java.util.ArrayList

/**
 * Created by Administrator on 2018/8/25 0025.
 */
//全局变量page, 每次进入时都读取值
var page = 1

class LoadAlldataFragment : BaseFragments(), SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    override fun onLoadMore() {
        isLoadMore = true
        onRefresh()
    }

    private var isLoadMore = false

    private var listSize = 0

    private var currentPosition = 0

    private lateinit var mScrollListener: LoadMoreScrollListener

    private lateinit var footView: LoadAllDataTitleBinder

    private lateinit var dataSet: MutableSet<Any>

    private var isRefresh = false

    //加载后续数据
    override fun onRefresh() {
        if (isRefresh) return
        isRefresh = true
        val currentpage = if (isLoadMore) {
            (listSize / 10) + 1
        } else {
            1
        }
        PrefUtils.applyInt(context, PAGE, currentpage)
        Log.d("czh", "refresh currentPage = $currentpage")

        gankDataViewModel.getGankData("福利", 10, currentpage)


    }


    override fun onFragmentVisibleChange(isVisible: Boolean) {
        if (isVisible) {
            onRefresh()
        } else {

        }
        super.onFragmentVisibleChange(isVisible)
    }


    private fun obtainViewModel(): LoadAllDataViewModel = ViewModelProviders
            .of(activity!!, ViewModelFactory.getInstance(application = activity!!.application))
            .get(LoadAllDataViewModel::class.java)

    lateinit var gankDataViewModel: LoadAllDataViewModel

    lateinit var mRefreshLayout: SwipeRefreshLayout

    lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: six.czh.com.gankio.testAdapter.MultiTypeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.frag_main, container, false)

        val layoutManager = GridLayoutManager(context, 2)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = mAdapter.getItem(position)
                return if (item is LoadAllDataTitleBean || item is String) 2 else 1
            }
        }
        mScrollListener = LoadMoreScrollListener().apply {
            setLayoutManager(layoutManager)
            setOnLoadMoreListener(this@LoadAlldataFragment)
        }
        val binder = object : LoadAllDataBinder() {
            override fun convert(holder: RecyclerView.ViewHolder, gankResult: GankResult) {
                with(holder.itemView.item_main_iv) {
                    Glide.with(holder.itemView.context)
                            .load(gankResult.url)
                            .placeholder(R.color.color_glide_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(400, 600)
                            .into(this)

                    setOnClickListener {
                        gankDataViewModel.openDetailUi(mAdapter.items as List<GankResult>, mAdapter.items.indexOf(gankResult))
                    }
                }
            }
        }

        //注册两种布局(一对多)
//        val group = object : OneToManyItemViewGroup<GankResult>(binder, LoadAllDataTitleBinder()){
//            override fun getViewHolderIndex(item: GankResult?): Int {
//                return if (item!!.desc.startsWith("2018-11")) 0 else 1
//            }
//
//        }

        mAdapter = six.czh.com.gankio.testAdapter.MultiTypeAdapter()

        mAdapter.register(LoadAllDataTitleBean::class.java,
                LoadAllDataTitleBinder(this))

        mAdapter.register(
                GankResult::class.java,
                binder)

        mAdapter.register(String::class.java,
                EmptyVieWBinder(this))

        dataSet = mutableSetOf()
//        dataSet.add("ttttt")



        mAdapter.addFooterItem(LoadAllDataTitleBean(0,"进度条"))


        mRecyclerView = root.findViewById<RecyclerView>(R.id.main_recycler).apply {
            adapter = mAdapter
            this.layoutManager = layoutManager
            addOnScrollListener(mScrollListener)

        }



        mRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.main_refresh).apply {
            setOnRefreshListener(this@LoadAlldataFragment)
        }
        rootView = root
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gankDataViewModel = obtainViewModel().apply {
            obtainViewModel().detailActivityReener.observe(this@LoadAlldataFragment.activity!!, Observer {
                currentPosition = it!!.getIntExtra(DetailDataActivity.CURRENT_ITEM, 0)

                if (currentPosition < mRecyclerView.adapter!!.itemCount) {
                    mRecyclerView.scrollToPosition(currentPosition)
                }
            })

            obtainViewModel().openDetailUi.observe(this@LoadAlldataFragment.activity!!, Observer {
                val intent = Intent()
                intent.setClass(context, DetailDataActivity::class.java)
                intent.putParcelableArrayListExtra("gankPhotos", it?.gankPhotos as ArrayList<Parcelable>)
                intent.putExtra("position", it.position)
                startActivityForResult(intent, DetailDataActivity.REQUEST_DETAIL_DATA)
            })

            obtainViewModel().errorMessage.observe(this@LoadAlldataFragment.activity!!, Observer {
                if (mAdapter.items.size  > 0 && mAdapter.footItems.size == 1) {
                    //说明是失败的，则显示刷新按钮
                    mAdapter.footItems[0] = LoadAllDataTitleBean(1, "刷新")
                }
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })

            obtainViewModel().gankResults.observe(this@LoadAlldataFragment.activity!!, Observer<List<GankResult>> {
                isRefresh = false
                isLoadMore = false
                if (it != null) {
                    Log.d("czh", "data.size = " + it.size)
                    if (it.size > listSize && mAdapter.footItems.size == 1) {
                        //说明有新数据，则不显示刷新按钮，显示进度条
                        mAdapter.footItems[0] = LoadAllDataTitleBean(0, "刷新")
                    }

                    if (it.isEmpty()) {
                        dataSet.clear()
                        dataSet.add("tttt")
                    } else {
                        dataSet.remove("tttt")
                    }
                    dataSet.addAll(it)
                    listSize = dataSet.size
                    mRefreshLayout.isRefreshing = false
                    mScrollListener.isLoading = false
                    mAdapter.items = dataSet.toList()
                    mAdapter.notifyDataSetChanged()

                    if (currentPosition != 0) {
                        mRecyclerView.scrollToPosition(currentPosition)
                        currentPosition = 0
                    }
                }

            })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        obtainViewModel().handleActivityResult(requestCode, resultCode, data)
    }

}

