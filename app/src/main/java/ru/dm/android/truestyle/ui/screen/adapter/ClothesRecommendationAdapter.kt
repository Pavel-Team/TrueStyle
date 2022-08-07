/**Адаптер для RecyclerView с рекомендуемой одеждой на странице с рекомендациями*/
package ru.dm.android.truestyle.ui.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.databinding.ItemClothesRecommendationBinding
import ru.dm.android.truestyle.model.ClothesRecommendation
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.holder.ClothesHolder

class ClothesRecommendationAdapter(private val context: Context,
                                   private val listClothes: List<Stuff>): RecyclerView.Adapter<ClothesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesHolder {
        val binding = DataBindingUtil.inflate<ItemClothesRecommendationBinding>(
            LayoutInflater.from(context),
            R.layout.item_clothes_recommendation,
            parent,
            false
        )
        return ClothesHolder(binding)
    }

    override fun onBindViewHolder(holder: ClothesHolder, position: Int) {
        val clothes = listClothes[position]
        holder.bind(clothes)
    }

    override fun getItemCount(): Int {
        return listClothes.size
    }
}