package six.czh.com.gankio.mainPage

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.RelativeLayout

/**
 * Created by oneplus on 19-1-2.
 * Email: six.cai@oneplus.com
 */
class DetailActivity: Activity() {

    private lateinit var mWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mWebView = WebView(this)
        val layout = RelativeLayout(this)
        layout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layout.addView(mWebView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        if (intent != null) {
            mWebView.loadUrl(intent.getStringExtra("url"))

        }


        setContentView(layout)
    }
}