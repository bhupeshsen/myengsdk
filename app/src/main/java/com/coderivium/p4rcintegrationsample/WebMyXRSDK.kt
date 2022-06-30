package com.coderivium.p4rcintegrationsample

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar


class WebMyXRSDK : AppCompatActivity() {

    lateinit var dialog: Dialog
    private lateinit var webView: WebView

    private lateinit var refresh: AppCompatImageView

    private lateinit var homeClick: AppCompatImageView
    private lateinit var backClick: AppCompatImageView
    private lateinit var forwardClick: AppCompatImageView
    private lateinit var toolbar: Toolbar
    private lateinit var webUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_viiew)
        webView=findViewById(R.id.web_view)
        backClick=findViewById(R.id.backClick)
        refresh=findViewById(R.id.refresh)
        homeClick=findViewById(R.id.homeClick)
        forwardClick=findViewById(R.id.forwardClick)
        toolbar=findViewById(R.id.toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener({ onBackPressed() })
        }
        setupWebView()
        createProgressDialog();
        loadPaymentWebView("https://myxr-web-stage.kiwi-internal.com")

        backClick.setOnClickListener {
            webView.goBack()
        }
        forwardClick.setOnClickListener {
            webView.goForward()
        }

        refresh.setOnClickListener {
            webView.reload()
        }

        homeClick.setOnClickListener {
            loadPaymentWebView("https://myxr-web-stage.kiwi-internal.com")
        }
    }
    private fun createProgressDialog() {
        val builder = AlertDialog.Builder(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(R.layout.progress)
        }
        dialog = builder.create()
        dialog.setCancelable(false)
        //dialog.window?.decorView?.setBackgroundResource(android.R.color.transparent);
        // dialog.window?.setDimAmount(0.0f)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) return true
                return false
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            setSupportZoom(false)
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                Log.e("url name",url.toString())

            }


        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                //Make the bar disappear after URL is loaded
                progressDialog(true)
                // Return the app name after finish loading
                if (progress == 100) {
                    progressDialog(false)
                }
            }
        }

    }
    fun progressDialog(show: Boolean) {
        if (show)
            dialog.show()
        else
            dialog.dismiss()
    }

    private fun loadPaymentWebView(url: String) {
        webView.loadUrl(url)
    }

    override fun onBackPressed() {

        if (webView.copyBackForwardList().getCurrentIndex() > 0) {
            webView.goBack();
        }
        else {
            // Your exit alert code, or alternatively line below to finish
            super.onBackPressed(); // finishes activity
        }
    }
}