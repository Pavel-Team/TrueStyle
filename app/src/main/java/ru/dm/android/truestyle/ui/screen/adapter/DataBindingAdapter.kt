package ru.dm.android.truestyle.ui.screen.adapter

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter


@SuppressLint("SetJavaScriptEnabled")
@BindingAdapter("setWebViewClient")
fun setWebViewClient(view: WebView, client: WebChromeClient) {
    view.webChromeClient = client
    view.webViewClient = WebViewClient()
    //view.settings.javaScriptEnabled=true
}

@BindingAdapter("loadUrl")
fun loadUrl(view: WebView, url: String) {
    view.loadUrl(url)
}


@BindingAdapter("maxProgress")
fun maxProgress(view: ProgressBar, maxProgress: Int) {
    view.max=maxProgress
}
