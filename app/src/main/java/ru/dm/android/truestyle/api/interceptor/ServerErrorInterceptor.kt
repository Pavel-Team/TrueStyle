package ru.dm.android.truestyle.api.interceptor

import android.util.Log
import androidx.fragment.app.FragmentManager
import okhttp3.Interceptor
import okhttp3.Response
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.ErrorServerDialogFragment

class ServerErrorInterceptor(private val fm: FragmentManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        Log.d("ErrorServer", response.code().toString())
        if (response.code() >= 500) {
            ErrorServerDialogFragment().apply {
                show(fm, ConstantsDialog.DIALOG_ERROR_SERVER)
            }
        }
        return response
    }
}