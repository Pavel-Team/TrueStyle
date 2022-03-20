/**Holder одного элемента одежды на странице рекомендаций*/
package ru.dm.android.truestyle.ui.screen.holder

import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.databinding.ItemClothesRecommendationBinding
import ru.dm.android.truestyle.model.Clothes
import ru.dm.android.truestyle.model.ClothesRecommendation
import ru.dm.android.truestyle.ui.screen.RecommendationFragmentDirections

class ClothesHolder(private val binding: ItemClothesRecommendationBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(clothes: ClothesRecommendation) {
        binding.apply {
            model = clothes
            executePendingBindings()
        }
    }

    override fun onClick(view: View?) {
        val navController = Navigation.findNavController(itemView)
        val id = binding.model!!.id

        val action = RecommendationFragmentDirections.actionNavigationRecommendationToFragmentClothes()
        action.clothesId = id

        navController.navigate(action)
    }

}