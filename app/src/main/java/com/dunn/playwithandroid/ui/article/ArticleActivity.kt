package com.dunn.playwithandroid.ui.article

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import com.dunn.playwithandroid.R
import com.dunn.playwithandroid.base.activity.BaseActivity
import com.dunn.playwithandroid.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_article.*

class ArticleActivity : BaseActivity() {

    private lateinit var title: String
    private lateinit var link: String

    companion object {
        fun start(context: BaseActivity, title: String, link: String) {
            val intent = Intent(context, ArticleActivity::class.java)
            intent.apply {
                putExtra("title", title)
                putExtra("link", link)
            }
            context.startActivity(intent)
        }
    }

    override fun initLayoutResId(): Int {
        return R.layout.activity_article
    }

    override fun initData() {
        intent?.let {
            title = it.getStringExtra("title")
            link = it.getStringExtra("link")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        initToolbar(Html.fromHtml(title).toString())

        articleWebView.requestFocusFromTouch()
        articleWebView.settings.run {
            javaScriptEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = true
        }

        articleWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                statusView.showErrorView()
            }
        }

        articleWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress > 90) {
                    statusView.showContentView()
                }
            }
        }
        articleWebView.loadUrl(link)

        initStatusView(R.id.articleWebView) {
            statusView.showLoadingView()
            articleWebView.loadUrl(link)
        }
        statusView.showLoadingView()
    }

    override fun initLoad() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setIconsVisible(menu!!, true)
        menuInflater.inflate(R.menu.article_menu_layout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_share -> share()
            R.id.menu_copy -> copy()
            R.id.menu_browser -> browser()
        }
        return true
    }

    private fun browser() {
        val intent = Intent()
        intent.run {
            data = Uri.parse(link)
            action = Intent.ACTION_VIEW
        }
        startActivity(intent)
    }

    private fun copy() {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.primaryClip = ClipData.newPlainText(null, link)
        ToastUtil.show(context, R.string.copy_success)
    }

    private fun share() {
        val intent = Intent()
        intent.run {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$title\n$link")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_to)))
    }


    /**
     * 通过反射使菜单显示图标
     */
    private fun setIconsVisible(menu: Menu, flag: Boolean) {
        try {
            val method = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
            method.isAccessible = true
            method.invoke(menu, flag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
