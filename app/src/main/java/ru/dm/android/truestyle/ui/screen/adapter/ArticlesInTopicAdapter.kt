package ru.dm.android.truestyle.ui.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemArticleInTopicBinding
import ru.dm.android.truestyle.model.ArticleInTopic
import ru.dm.android.truestyle.ui.screen.holder.ArticleInTopicHolder

class ArticlesInTopicAdapter(private val context: Context): ListAdapter<ArticleInTopic,ArticleInTopicHolder>(DiffCallbackArticles())  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleInTopicHolder {
        val binding = DataBindingUtil.inflate<ItemArticleInTopicBinding>(
            LayoutInflater.from(context),
            R.layout.item_article_in_topic,
            parent,
            false
        )
        return ArticleInTopicHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ArticleInTopicHolder, position: Int) {
        val articles = currentList[position]
        holder.bind(articles)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }


    private class DiffCallbackArticles : DiffUtil.ItemCallback<ArticleInTopic>() {

        //Проверка, являются ли два объекта одним и тем же элементом
        override fun areItemsTheSame(oldItem: ArticleInTopic, newItem: ArticleInTopic) =
            oldItem.id == newItem.id

        //Имееются ли одинаковые данные
        override fun areContentsTheSame(oldItem: ArticleInTopic, newItem: ArticleInTopic) =
            oldItem == newItem
    }
}