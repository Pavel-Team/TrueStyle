package ru.dm.android.truestyle.thread

import android.os.AsyncTask
import ru.dm.android.truestyle.repository.RegistrationRepository

class CheckEmailAsyncTask: AsyncTask<String, Void, Boolean>() {

    override fun doInBackground(vararg params: String): Boolean {
        val registrationRepository = RegistrationRepository
        return registrationRepository.checkEmail(params[0])
    }
}
