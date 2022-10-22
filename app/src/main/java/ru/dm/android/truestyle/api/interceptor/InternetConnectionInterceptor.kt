package ru.dm.android.truestyle.api.interceptor

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import okhttp3.*
import ru.dm.android.truestyle.service.SystemService
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.ErrorServerDialogFragment

class InternetConnectionInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!SystemService.isInternetAvailable(context)) {
            return Response.Builder()
                .code(418)
                .protocol(Protocol.HTTP_2)
                .message("No internet")
                .body(ResponseBody.create(MediaType.parse("text/plain"), "No internet connection"))
                .request(chain.request())
                .build()
        }
        val response = chain.proceed(chain.request())
        return response
    }
}