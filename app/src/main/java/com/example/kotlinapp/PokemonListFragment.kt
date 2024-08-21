package com.example.kotlinapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.FragmentInfoBinding
import com.example.kotlinapp.databinding.FragmentMainBinding
import java.util.concurrent.Executors

class PokemonListFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val executor = Executors.newSingleThreadExecutor()
    private val pokemonsNetwork = PokemonsNetwork()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
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
        recyclerView.adapter = adapter

        val limit = 20

        executor.submit {
            try {
                val pokemons = pokemonsNetwork.getPokemons(limit)
                requireActivity().runOnUiThread() {
                    adapter.setPokemons(pokemons)
                }
            } catch (e: Exception) {
                handleNetworkError()
            }
        }
    }

    private fun handleNetworkError() {
        requireActivity().runOnUiThread() {
            Toast.makeText(requireActivity(), "Ошибка сети", Toast.LENGTH_LONG).show()
        }
    }
}