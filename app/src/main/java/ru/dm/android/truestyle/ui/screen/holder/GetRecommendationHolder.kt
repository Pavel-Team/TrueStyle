package ru.dm.android.truestyle.ui.screen.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemClothesInGetRecommendationBinding
import ru.dm.android.truestyle.model.GetRecommendationClothes
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.ui.screen.ClothesFragment

class GetRecommendationHolder(private val binding: ItemClothesInGetRecommendationBinding,
                              context: Context): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private var callbacks: NavigationCallbacks

    init {
        itemView.setOnClickListener(this)
        callbacks = context as NavigationCallbacks
    }


    fun bind(getRecommendationClothes: GetRecommendationClothes) {
        binding.apply {
            model = getRecommendationClothes
            executePendingBindings()

            if (getRecommendationClothes.id == 2)
                imageViewGetRecommendationClothes.setImageResource(R.drawable.example_clothes_2)
        }
    }


    override fun onClick(p0: View?) {
        val id = binding.model!!.id

        val fragmentTo = ClothesFragment.newInstance(id)
        callbacks.navigateTo(fragmentTo, R.id.navigation_clothes_search)
    }
}