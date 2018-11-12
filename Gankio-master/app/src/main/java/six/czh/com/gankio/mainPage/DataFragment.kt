package six.czh.com.gankio.mainPage

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import six.czh.com.gankio.R

/**
 * Created by six.cai on 18-11-12.
 * Email: baicai94@foxmail.com
 */
class DataFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.frag_main, container, false)


        return root
    }
}