/**Адаптер для RecyclerView с рекомендуемыми статьями на странице с рекомендациями*/
package ru.dm.android.truestyle.ui.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemArticleRecommendationBinding
import ru.dm.android.truestyle.model.ArticleRecommendation
import ru.dm.android.truestyle.ui.screen.holder.ArticleHolder

class ArticleRecommendationAdapter(private val context: Context,
                                   private val listArticles: List<ArticleRecommendation>): RecyclerView.Adapter<ArticleHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val binding = DataBindingUtil.inflate<ItemArticleRecommendationBinding>(
            LayoutInflater.from(context),
            R.layout.item_article_recommendation,
            parent,
            false
        )
        return ArticleHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article = listArticles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return listArticles.size
    }
}