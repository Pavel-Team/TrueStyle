/**Holder одного элемента одежды на странице рекомендаций*/
package ru.dm.android.truestyle.ui.screen.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemClothesRecommendationBinding
import ru.dm.android.truestyle.model.ClothesRecommendation
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.ui.screen.ClothesFragment

class ClothesHolder(private val binding: ItemClothesRecommendationBinding, context: Context): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private var callbacks: NavigationCallbacks

    init {
        itemView.setOnClickListener(this)
        callbacks = context as NavigationCallbacks
    }

    fun bind(clothes: ClothesRecommendation) {
        binding.apply {
            model = clothes
            executePendingBindings()
            if (clothes.id == 1)
                imageViewRecommendedClothes.setImageResource(R.drawable.example_clothes_1)
            else if (clothes.id == 2)
                imageViewRecommendedClothes.setImageResource(R.drawable.example_clothes_2)
            else if (clothes.id == 3)
                imageViewRecommendedClothes.setImageResource(R.drawable.example_clothes_3)
        }
    }

    override fun onClick(view: View?) {
        val id = binding.model!!.id

        val fragmentTo = ClothesFragment.newInstance(id)
        callbacks.navigateTo(fragmentTo, R.id.navigation_recommendation)
    }

}