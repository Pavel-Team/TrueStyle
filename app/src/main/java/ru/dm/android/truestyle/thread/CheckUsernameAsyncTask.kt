package ru.dm.android.truestyle.thread

import android.os.AsyncTask
import ru.dm.android.truestyle.repository.RegistrationRepository

class CheckUsernameAsyncTask: AsyncTask<String, Void, Boolean>() {

    override fun doInBackground(vararg params: String): Boolean {
        val registrationRepository = RegistrationRepository
        return registrationRepository.checkUsername(params[0])
    }

}