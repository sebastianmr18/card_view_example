package com.example.class_7.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.class_7.R
import com.example.class_7.RecyclerAdapter
import com.example.class_7.databinding.FragmentRecyclerBinding
import com.example.class_7.SuperHero

class RecyclerFragment : Fragment() {
    private lateinit var binding: FragmentRecyclerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecyclerBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler()
    }

    fun recycler() {
        var heroesList = mutableListOf(
            SuperHero("SuperMan","Superman es conocido por su increíble fuerza física. Puede levantar objetos extremadamente pesados, desde automóviles hasta edificios.", R.drawable.superman),
            SuperHero("Hulk", "Radica en su capacidad para transformarse de un físico débil y tímido llamado Bruce Banner en un gigante verde y musculoso conocido como Hulk", R.drawable.hulk),
            SuperHero("Batman", "En lugar de depender de poderes extraordinarios, el superhéroe Batman confía en su inteligencia, entrenamiento físico y habilidades técnicas para combatir el crimen", R.drawable.batman),
            SuperHero("Capitan América","No posee poderes sobrenaturales, ha sido mejorado físicamente a través de un suero de súper soldado", R.drawable.capitan),
            SuperHero("Pantera Negra","Fuerza Sobrehumana, Sentidos Agudizados, Agilidad y Velocidad Mejoradas,Maestría en Artes Marciales", R.drawable.pantera),
            SuperHero("Spider-Man", "Sentido arácnido, agilidad", R.drawable.spiderman)
        )

        val recycler = binding.recyclerview
        recycler.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerAdapter(heroesList)
        recycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}