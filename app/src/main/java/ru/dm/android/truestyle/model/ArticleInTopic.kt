/**Модель статьи во вкладке со всеми статьями для заданной темы*/
package ru.dm.android.truestyle.model

data class ArticleInTopic(val id: Int,
                          val title: String,
                          val description: String,
                          val imageUrl: String) {
}