package ru.dm.android.truestyle.ui.screen.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.util.Constants


@SuppressLint("SetJavaScriptEnabled")
@BindingAdapter("setWebViewClient")
fun setWebViewClient(view: WebView, client: WebChromeClient) {
    view.webChromeClient = client
    view.webViewClient = WebViewClient()
    view.settings.javaScriptEnabled=true
}

@BindingAdapter("loadUrl")
fun loadUrl(view: WebView, url: String) {
    view.loadUrl(url)
}


@BindingAdapter("maxProgress")
fun maxProgress(view: ProgressBar, maxProgress: Int) {
    view.max=maxProgress
}


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        val fullUrl = Constants.URL.removeSuffix("/") + url.toString()

        val glideUrl = GlideUrl(
            fullUrl,
            LazyHeaders.Builder()
                .addHeader("Authorization", Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(view.context))
                .build()
        )

        Log.d("Recom", fullUrl)

        Glide.with(view.context)
            .load(glideUrl)
            .placeholder(R.drawable.example_clothes)
            .into(view)
    }
}
