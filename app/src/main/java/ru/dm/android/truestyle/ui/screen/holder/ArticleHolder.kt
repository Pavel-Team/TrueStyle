/**Holder одного элемента статьи на странице рекомендаций*/
package ru.dm.android.truestyle.ui.screen.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.databinding.ItemArticleRecommendationBinding
import ru.dm.android.truestyle.model.ArticleRecommendation

class ArticleHolder(private val binding: ItemArticleRecommendationBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener{
    init {
        itemView.setOnClickListener(this)
    }

    fun bind(article: ArticleRecommendation) {
        binding.apply {
            model = article
            executePendingBindings()
        }
    }

    override fun onClick(view: View?) {
        //...
    }
}