package ru.dm.android.truestyle.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import ru.dm.android.truestyle.service.SystemService

class InternetConnectionInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!SystemService.isInternetAvailable(context)) {
            return Response.Builder()
                .code(418)
                .protocol(Protocol.HTTP_2)
                .message("No internet")
                .request(chain.request())
                .build()
        }
        return chain.proceed(chain.request())
    }
}