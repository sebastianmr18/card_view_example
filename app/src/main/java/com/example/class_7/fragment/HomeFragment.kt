package com.example.class_7.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.class_7.R
import com.example.class_7.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToRecycler()
    }

    private fun navigateToRecycler() {
        binding.buttonFragmentRecycler.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_recycler_Fragment)
        }
    }
}