package ru.dm.android.truestyle.ui.screen.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemClothesInWardrobeBinding
import ru.dm.android.truestyle.model.WardrobeClothes
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.ClothesFragment

class WardrobeClothesHolder(val navigation: Navigation,
                            private val binding: ItemClothesInWardrobeBinding,
                            context: Context): RecyclerView.ViewHolder(binding.root), View.OnClickListener  {

    init {
        itemView.setOnClickListener(this)
        
    }

    fun bind(wardrobeClothes: WardrobeClothes) {
        binding.apply {
            model = wardrobeClothes
            executePendingBindings()

            if (wardrobeClothes.id==2)
                imageViewWardrobe.setImageResource(R.drawable.example_clothes_wardrobe_2)
        }
    }

    override fun onClick(view: View?) {
        val id = binding.model!!.id

        val fragmentTo = ClothesFragment.newInstance(id)
        navigation.navigateTo(fragmentTo, R.id.navigation_profile)
    }
}