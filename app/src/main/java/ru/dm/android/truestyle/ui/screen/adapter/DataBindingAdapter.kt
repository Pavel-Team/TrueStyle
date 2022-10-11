package ru.dm.android.truestyle.ui.screen.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.android.material.progressindicator.CircularProgressIndicator
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
    Log.d("ArticleD", url)
    view.loadUrl(Constants.URL + url)
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

        val indicatorLoading = CircularProgressDrawable(view.context)
        indicatorLoading.centerRadius = 40f
        indicatorLoading.strokeWidth = 8f
        indicatorLoading.alpha = 90
        //indicatorLoading.setColorSchemeColors(view.context.getColor(R.color.light_blue))
        indicatorLoading.start()

        Glide.with(view.context)
            .load(glideUrl)
            .placeholder(indicatorLoading)
            .into(view)
    }
}


@BindingAdapter("imageUrlClothes")
fun loadImageClothes(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        val fullUrl = Constants.URL + "wardrobe/img" + url.toString()

        val glideUrl = GlideUrl(
            fullUrl,
            LazyHeaders.Builder()
                .addHeader("Authorization", Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(view.context))
                .build()
        )

        Log.d("Recom", fullUrl)

        val indicatorLoading = CircularProgressDrawable(view.context)
        indicatorLoading.centerRadius = 40f
        indicatorLoading.strokeWidth = 8f
        indicatorLoading.alpha = 90
        //indicatorLoading.setColorSchemeColors(view.context.getColor(R.color.light_blue))
        indicatorLoading.start()

        Glide.with(view.context)
            .load(glideUrl)
            .placeholder(indicatorLoading)
            .into(view)
    }
}
