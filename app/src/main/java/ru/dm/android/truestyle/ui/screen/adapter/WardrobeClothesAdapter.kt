package ru.dm.android.truestyle.ui.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemClothesInWardrobeBinding
import ru.dm.android.truestyle.model.WardrobeClothes
import ru.dm.android.truestyle.ui.screen.holder.WardrobeClothesHolder

class WardrobeClothesAdapter(private val context: Context): ListAdapter<WardrobeClothes, WardrobeClothesHolder>(DiffCallbackWardrobeClothes())  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WardrobeClothesHolder {
        val binding = DataBindingUtil.inflate<ItemClothesInWardrobeBinding>(
            LayoutInflater.from(context),
            R.layout.item_clothes_in_wardrobe,
            parent,
            false
        )
        return WardrobeClothesHolder(binding, context)
    }

    override fun onBindViewHolder(holder: WardrobeClothesHolder, position: Int) {
        val articles = currentList[position]
        holder.bind(articles)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }


    private class DiffCallbackWardrobeClothes : DiffUtil.ItemCallback<WardrobeClothes>() {

        override fun areItemsTheSame(oldItem: WardrobeClothes, newItem: WardrobeClothes) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: WardrobeClothes, newItem: WardrobeClothes) =
            oldItem == newItem
    }
}