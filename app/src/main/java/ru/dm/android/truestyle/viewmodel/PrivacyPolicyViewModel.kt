package ru.dm.android.truestyle.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.util.Constants

class PrivacyPolicyViewModel(application: Application): AndroidViewModel(application) {

    val liveDataHtmlPrivacyPolicy: MutableLiveData<String> = MutableLiveData("")

}