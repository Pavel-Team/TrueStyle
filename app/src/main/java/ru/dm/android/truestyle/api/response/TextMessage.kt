/**Модель для получения ответа в виде строки*/
package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class TextMessage(@SerializedName("message") val message: String) {
}