package ru.dm.android.truestyle.ui.screen.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemTopicBinding
import ru.dm.android.truestyle.model.Topic
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.ui.screen.ArticlesInTopicFragment

class TopicHolder(private val binding: ItemTopicBinding, context: Context): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private var callbacks: NavigationCallbacks

    init {
        itemView.setOnClickListener(this)
        callbacks = context as NavigationCallbacks
    }

    fun bind(topic: Topic) {
        binding.apply {
            model = topic
            executePendingBindings()
            if (topic.id == 1)
                imageViewTopic.setImageResource(R.drawable.example_topic_1)
            else if (topic.id == 2)
                imageViewTopic.setImageResource(R.drawable.example_topic_2)
        }
    }

    override fun onClick(view: View?) {
        val titleTopic = binding.model!!.title

        val fragmentTo = ArticlesInTopicFragment.newInstance(titleTopic)
        callbacks.navigateTo(fragmentTo, R.id.navigation_articles)
    }

}