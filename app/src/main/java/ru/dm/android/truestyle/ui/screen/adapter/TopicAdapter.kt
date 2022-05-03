package ru.dm.android.truestyle.ui.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemTopicBinding
import ru.dm.android.truestyle.model.Topic
import ru.dm.android.truestyle.ui.screen.holder.TopicHolder

class TopicAdapter(private val context: Context,
                   private val listTopics: List<Topic>): RecyclerView.Adapter<TopicHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicHolder {
        val binding = DataBindingUtil.inflate<ItemTopicBinding>(
            LayoutInflater.from(context),
            R.layout.item_topic,
            parent,
            false
        )
        return TopicHolder(binding, context)
    }

    override fun onBindViewHolder(holder: TopicHolder, position: Int) {
        val topics = listTopics[position]
        holder.bind(topics)
    }

    override fun getItemCount(): Int {
        return listTopics.size
    }

}