package ru.dm.android.truestyle.thread

import android.os.AsyncTask
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.model.Registration
import ru.dm.android.truestyle.repository.RegistrationRepository

class RegistrationAsyncTask: AsyncTask<String, Void, Auth?>() {

    val registrationRepository = RegistrationRepository

    override fun doInBackground(vararg params: String): Auth? {
        return registrationRepository.registerUser(params[0], params[1], params[2])
    }

}