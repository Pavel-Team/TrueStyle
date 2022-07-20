package ru.dm.android.truestyle.ui.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.databinding.ItemClothesInGetRecommendationBinding
import ru.dm.android.truestyle.model.GetRecommendationClothes
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.holder.GetRecommendationHolder

class GetRecommendationAdapter(private val context: Context,
                               private val listRecommendedClothes: List<Stuff>): RecyclerView.Adapter<GetRecommendationHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetRecommendationHolder {
        val binding = DataBindingUtil.inflate<ItemClothesInGetRecommendationBinding>(
            LayoutInflater.from(context),
            R.layout.item_clothes_in_get_recommendation,
            parent,
            false
        )
        return GetRecommendationHolder(binding)
    }

    override fun onBindViewHolder(holder: GetRecommendationHolder, position: Int) {
        val clothes = listRecommendedClothes[position]
        holder.bind(clothes)
    }

    override fun getItemCount(): Int {
        return listRecommendedClothes.size
    }

}