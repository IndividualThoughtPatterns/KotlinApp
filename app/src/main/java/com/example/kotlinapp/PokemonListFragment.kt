package com.example.kotlinapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.FragmentMainBinding
import androidx.recyclerview.widget.LinearLayoutManager

class PokemonListFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val adapter = PokemonAdapter(onPokemonClick = { pokemon: Pokemon ->
            val bundle = Bundle()
            bundle.putSerializable("pokemon", pokemon)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment, PokemonInfoFragment::class.java, bundle)
                .addToBackStack("PokemonListFragment").commit()
        })

        val limit = 20

        PokemonsNetwork().getPokemons(limit, onResult = { pokemons ->
            requireActivity().runOnUiThread() {
                recyclerView.adapter = adapter
                adapter.setPokemons(pokemons)
            }
        }, onError = {
            handleNetworkError()
        })
    }

    private fun handleNetworkError() {
        requireActivity().runOnUiThread() {
            Toast.makeText(requireActivity(), "Ошибка сети", Toast.LENGTH_LONG).show()
        }
    }
}