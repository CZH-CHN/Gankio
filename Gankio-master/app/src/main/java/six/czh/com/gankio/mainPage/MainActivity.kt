package six.czh.com.gankio.mainPage

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_news.*
import six.czh.com.gankio.R
import six.czh.com.gankio.ViewModelFactory
import six.czh.com.gankio.loadAllData.LoadAlldataFragment

/**
 * Created by six.cai on 18-11-12.
 * Email: baicai94@foxmail.com
 */
class MainActivity: AppCompatActivity() {

    private var fragmentList = mutableListOf<Fragment>()

    private lateinit var mMainDataViewModel: MainDataViewModel

    private lateinit var mAdapter: MainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        fragmentList.add(LoadAlldataFragment())

        news_tab.addTab(news_tab.newTab().setText("福利"))

        val androidFragment = DataFragment().apply {
            arguments = Bundle().apply {
                putString("EXTRA_TYPE", "Android")
            }
        }
        fragmentList.add(androidFragment)

        news_tab.addTab(news_tab.newTab().setText("Android"))

        val iosFragment = DataFragment().apply {
            arguments = Bundle().apply {
                putString("EXTRA_TYPE", "iOS")
            }
        }

        fragmentList.add(iosFragment)

        news_tab.addTab(news_tab.newTab().setText("iOS"))
        mAdapter = MainFragmentAdapter(supportFragmentManager)

        news_viewpager.apply {
            adapter = mAdapter
        }

        news_tab.setupWithViewPager(news_viewpager)
        //手动添加标题 ,必须在setupwidthViewPager之后否则不行

        news_tab.getTabAt(0)?.text = "福利"
        news_tab.getTabAt(1)?.text = "Android"
        news_tab.getTabAt(2)?.text = "iOS"



        mMainDataViewModel = obtainViewModel().apply {

        }
    }

    private fun obtainViewModel(): MainDataViewModel = ViewModelProviders
            .of(this, ViewModelFactory.getInstance(application = this.application))
            .get(MainDataViewModel::class.java)

    inner class MainFragmentAdapter(private val fm: FragmentManager): FragmentPagerAdapter(fm) {
        override fun getItem(p0: Int): Fragment {
            return fragmentList[p0]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return news_tab.getTabAt(position)?.text
        }

    }


}