package ru.dm.android.truestyle.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CropPhotoViewModel: ViewModel() {
    val liveDataPhotoUri: MutableLiveData<Uri> = MutableLiveData(null)
}