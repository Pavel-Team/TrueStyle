/**Holder одного элемента статьи на странице рекомендаций*/
package ru.dm.android.truestyle.ui.screen.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemArticleRecommendationBinding
import ru.dm.android.truestyle.model.ArticleRecommendation
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.ui.screen.ArticleFragment

class ArticleHolder(private val binding: ItemArticleRecommendationBinding, context: Context): RecyclerView.ViewHolder(binding.root), View.OnClickListener{

    private var callbacks: NavigationCallbacks

    init {
        itemView.setOnClickListener(this)
        callbacks = context as NavigationCallbacks
    }

    fun bind(article: ArticleRecommendation) {
        binding.apply {
            model = article
            executePendingBindings()
        }
    }

    override fun onClick(view: View?) {
        val id = binding.model!!.id

        val fragmentTo = ArticleFragment.newInstance(id)
        callbacks.navigateTo(fragmentTo, R.id.navigation_recommendation)
    }
}