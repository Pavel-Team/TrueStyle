package ru.dm.android.truestyle.ui.screen.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.databinding.ItemClothesInGetRecommendationBinding
import ru.dm.android.truestyle.model.GetRecommendationClothes
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.ClothesFragment

class GetRecommendationHolder(val navigation: Navigation,
                              private val binding: ItemClothesInGetRecommendationBinding,
                              context: Context): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
        
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
        val clothes = binding.model!!

        //ВРЕМЕННО!!!!!!!!
        val fragmentTo = ClothesFragment.newInstance(Stuff())
        navigation.navigateTo(fragmentTo, R.id.navigation_clothes_search)
    }
}