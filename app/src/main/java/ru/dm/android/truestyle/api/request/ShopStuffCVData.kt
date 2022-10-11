/**Модель для получения рекомендаций по фотке*/
package ru.dm.android.truestyle.api.request

import ru.dm.android.truestyle.api.response.Gender

data class ShopStuffCVData(val articleType: String = "",
                           val gender: Gender = Gender(),
                           val season: String = "",
                           var baseColor: String = "") {
}