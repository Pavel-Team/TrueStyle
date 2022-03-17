/**Holder одного элемента одежды на странице рекомендаций*/
package ru.dm.android.truestyle.ui.screen.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.databinding.ItemClothesRecommendationBinding
import ru.dm.android.truestyle.model.Clothes

class ClothesHolder(private val binding: ItemClothesRecommendationBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(clothes: Clothes) {
        binding.apply {
            model = clothes
            executePendingBindings()
        }
    }

    override fun onClick(view: View?) {
        //...
    }

}