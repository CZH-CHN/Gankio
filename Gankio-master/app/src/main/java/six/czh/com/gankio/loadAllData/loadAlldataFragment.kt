package six.czh.com.gankio.loadAllData

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.frag_main.*
import kotlinx.android.synthetic.main.item_main.view.*
import six.czh.com.gankio.R
import six.czh.com.gankio.data.GankResult
import six.czh.com.gankio.loadAllData.loadAlldataFragment.DataAdapter.MainViewHolder
import six.czh.com.gankio.loadAllData.scroll.OnLoadMoreListener
import six.czh.com.gankio.loadAllData.scroll.loadMoreScrollListener
import six.czh.com.myapplication.loadAllData.loadAlldataContract
import six.czh.com.myapplication.loadAllData.loadAlldataPresenter
import java.util.ArrayList

/**
 * Created by Administrator on 2018/8/25 0025.
 */
    var page = 1;
class loadAlldataFragment : Fragment(), loadAlldataContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private lateinit var mScrollListener : loadMoreScrollListener
    override fun onLoadMore() {

    }

    override fun onRefresh() {
        Log.d("czh", "refresh")
//        mRefreshLayout.isRefreshing = true
        presenter.loadMsg("福利", 10, page++)
    }

    override lateinit var presenter: loadAlldataContract.Presenter

    lateinit var mRefreshLayout : SwipeRefreshLayout

    override fun loadMsgSuccess(gankResultList : List<GankResult>) {
        Log.d("czh", "" + gankResultList.size)
        mRefreshLayout.isRefreshing = false
        mScrollListener.isLoading = false
        mAdapter.replaceData(gankResultList)
    }

    override fun loadMsgFail() {
        Toast.makeText(activity, "获取失败", Toast.LENGTH_LONG).show()
    }

    private lateinit var mAdapter : DataAdapter

    public lateinit var mDatalist : List<GankResult>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.frag_main, container, false)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mScrollListener = loadMoreScrollListener();
        mScrollListener.setLayoutManager(layoutManager);
//        mScrollListener.setOnLoadMoreListener ({
//            Log.d("czh", "onLoadMore")
//            presenter.loadMsg("福利", 10, page++)
//        })
        mScrollListener.setOnLoadMoreListener(object : OnLoadMoreListener{
            override fun onLoadMore() {
                Log.d("czh", "onLoadMore")
                presenter.loadMsg("福利", 10, page++)
            }
        })
        val recyclerview = root.findViewById<RecyclerView>(R.id.main_recycler)
        mAdapter = DataAdapter(ArrayList<GankResult>(0))

        recyclerview.layoutManager = layoutManager

        recyclerview.addOnScrollListener(mScrollListener)
        recyclerview.adapter = mAdapter
        mRefreshLayout = root.findViewById(R.id.main_refresh)

        mRefreshLayout.setOnRefreshListener(this@loadAlldataFragment)

        return root;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = loadAlldataPresenter(this@loadAlldataFragment)
        onRefresh()
    }

    private class DataAdapter(var DataList : ArrayList<GankResult>) : RecyclerView.Adapter<MainViewHolder>() {


        override fun getItemCount(): Int = DataList.size;

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            Log.d("czhhhh", "onCreateViewHolder ")
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)

            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val istrurl = DataList.get(position).url
            if (null == holder || null == istrurl || istrurl.equals("")) {
                return;
            }
            Log.d("czhhhhhhhhh", "position = " + DataList.get(position).toString())
            holder.bind(DataList.get(position))
        }

        fun replaceData(GankiodataList: List<GankResult>) {
            setList(GankiodataList)
            notifyDataSetChanged()
        }

        private fun setList(GankiodataList: List<GankResult>) {
            if(DataList.isEmpty()){
                DataList = ArrayList(GankiodataList)
            } else {
                DataList.addAll(GankiodataList)
            }

            Log.d("ccccc", "" + DataList.size)

        }

        private class MainViewHolder(var view: View) : RecyclerView.ViewHolder(view){

            fun bind(data : GankResult){
                Glide.with(view.context).load(data.url).diskCacheStrategy(DiskCacheStrategy.ALL).into(view.item_main_iv)
            }
        }
    }
}

