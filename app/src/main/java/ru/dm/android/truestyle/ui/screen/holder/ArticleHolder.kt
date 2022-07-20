/**Holder одного элемента статьи на странице рекомендаций*/
package ru.dm.android.truestyle.ui.screen.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Article
import ru.dm.android.truestyle.databinding.ItemArticleRecommendationBinding
import ru.dm.android.truestyle.model.ArticleRecommendation
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.ArticleFragment

class ArticleHolder(private val binding: ItemArticleRecommendationBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener{

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(article: Article) {
        binding.apply {
            model = article
            executePendingBindings()
        }
    }

    override fun onClick(view: View?) {
        val article = binding.model!!

        val fragmentTo = ArticleFragment.newInstance(article)
        Navigation.navigateTo(fragmentTo, R.id.navigation_recommendation)
    }
}