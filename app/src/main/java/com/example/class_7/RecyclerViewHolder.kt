package com.example.class_7

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.class_7.databinding.ItemSuperHeroBinding

class RecyclerViewHolder(binding: ItemSuperHeroBinding): RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding

    fun setHeroItem(superHero: SuperHero) {
        bindingItem.imageViewSuperHero.setImageResource(superHero.imageId)
        bindingItem.textViewSuperName.text = superHero.name
        bindingItem.textViewSuperPower.text = superHero.power

        bindingItem.cardViewSuperHeroes.setOnClickListener{
            Toast.makeText(it.context, "${superHero.name}", Toast.LENGTH_LONG).show()
        }
    }
}