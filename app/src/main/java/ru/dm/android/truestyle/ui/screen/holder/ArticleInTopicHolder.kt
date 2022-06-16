package ru.dm.android.truestyle.ui.screen.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemArticleInTopicBinding
import ru.dm.android.truestyle.databinding.ItemTopicBinding
import ru.dm.android.truestyle.model.ArticleInTopic
import ru.dm.android.truestyle.model.Topic
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.ui.screen.ArticleFragment

class ArticleInTopicHolder(private val binding: ItemArticleInTopicBinding, context: Context): RecyclerView.ViewHolder(binding.root), View.OnClickListener  {
    private var callbacks: NavigationCallbacks

    init {
        itemView.setOnClickListener(this)
        callbacks = context as NavigationCallbacks
    }

    fun bind(articleInTopic: ArticleInTopic) {
        binding.apply {
            model = articleInTopic
            executePendingBindings()

            if (articleInTopic.id == 2)
                imageViewArticleInTopic.setImageResource(R.drawable.example_article_in_topic_2)
            else if (articleInTopic.id == 3)
                imageViewArticleInTopic.setImageResource(R.drawable.example_article_in_topic_3)
        }
    }

    override fun onClick(view: View?) {
        val id = binding.model!!.id

        val fragmentTo = ArticleFragment.newInstance(id)
        callbacks.navigateTo(fragmentTo, R.id.navigation_articles)
    }
}