/**Holder одного элемента одежды на странице рекомендаций*/
package ru.dm.android.truestyle.ui.screen.holder

import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.databinding.ItemClothesRecommendationBinding
import ru.dm.android.truestyle.model.ClothesRecommendation
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.ClothesFragment

class ClothesHolder(private val binding: ItemClothesRecommendationBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(clothes: Stuff) {
        binding.apply {
            model = clothes
            executePendingBindings()
        }
    }

    override fun onClick(view: View?) {
        val clothes = binding.model!!

        val fragmentTo = ClothesFragment.newInstance(clothes)
        Navigation.navigateTo(fragmentTo, R.id.navigation_recommendation)
    }

}