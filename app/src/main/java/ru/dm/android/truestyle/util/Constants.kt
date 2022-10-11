package ru.dm.android.truestyle.util

object Constants {
    //Константы для подключения
    const val URL = "http://185.221.153.118:8080/"
    const val TYPE_TOKEN = "Bearer"

    //Константы для проверки версии
    const val MIN_DIFF_DATE_FOR_VISIBLE_DIALOG_APP_VERSION = 24 * 60* 60 * 1000L
    const val LINK_IN_PLAY_MARKET = "https://play.google.com/store/apps/details?id=yio.tro.antiyoy.android&hl=ru&gl=US"

    //Константы для названия сезона в БД на сервере
    const val SEASON_SUMMER = "лето"
    const val SEASON_WINTER = "зима"
    const val SEASON_SPRING = "весна"
    const val SEASON_AUTUMN = "осень"
    const val SEASON_NAN = "демисезон"

    //Константы пола
    const val GENDER_MAN = "Men"
    const val GENDER_WOMAN = "Women"

    //Константы для подписки
    const val TELEGRAM_CHANEL = "https://t.me/TrueStyleDev"

    //Константы для валидации
    val REGEX_EMAIL = Regex("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    //Константы для работы с файловой системой
    val FILE_NAME = "temporaryPhoto.jpg"
}