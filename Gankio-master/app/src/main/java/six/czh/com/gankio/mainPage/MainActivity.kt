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

        fragmentList.add(DataFragment())

        news_tab.addTab(news_tab.newTab().setText("测试111111"))

        fragmentList.add(DataFragment())

        news_tab.addTab(news_tab.newTab().setText("测试111111"))

        mAdapter = MainFragmentAdapter(supportFragmentManager)

        news_viewpager.apply {
            adapter = mAdapter
        }

        news_tab.setupWithViewPager(news_viewpager)

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
            return "23312"
        }

    }


}