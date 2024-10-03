package com.example.class_7

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.class_7.databinding.ItemSuperHeroBinding

class RecyclerAdapter(private val heroesList:MutableList<SuperHero>):
    RecyclerView.Adapter<RecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemSuperHeroBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return heroesList.size
    }

    override fun onBindViewHolder(
        recyclerViewHolder: RecyclerViewHolder,
        position: Int
    ) {
        val hero = heroesList[position]
        recyclerViewHolder.setHeroItem(hero)
    }

}