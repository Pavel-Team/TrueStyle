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

class ArticleHolder(val navigation: Navigation,
                    private val binding: ItemArticleRecommendationBinding,
                    context: Context): RecyclerView.ViewHolder(binding.root), View.OnClickListener{

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(article: Article) {
        binding.apply {
            model = article
            executePendingBindings()

//            if (article.id == 2)
//                imageViewArticleRecommended.setImageResource(R.drawable.example_article_2)
//            else if (article.id == 3)
//                imageViewArticleRecommended.setImageResource(R.drawable.example_article_3)
//            else if (article.id == 4)
//                imageViewArticleRecommended.setImageResource(R.drawable.example_article_4)
//            else if (article.id == 5)
//                imageViewArticleRecommended.setImageResource(R.drawable.example_article_5)
        }
    }

    override fun onClick(view: View?) {
        val id = binding.model!!.id

        val fragmentTo = ArticleFragment.newInstance(id)
        navigation.navigateTo(fragmentTo, R.id.navigation_recommendation)
    }
}