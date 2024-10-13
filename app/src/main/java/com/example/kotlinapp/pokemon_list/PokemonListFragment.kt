package com.example.kotlinapp.pokemon_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.App
import com.example.kotlinapp.databinding.FragmentMainBinding


class PokemonListFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.recyclerView
        App.pokemonListViewModel = ViewModelProvider(
            requireActivity(),
            PokemonListViewModelFactory(
                fragment = this,
                app = requireActivity().application,
                handleNetworkError = ::handleNetworkError
            )
        )[PokemonListViewModel::class.java]
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = App.pokemonListViewModel.adapter

        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1) &&
                        newState == RecyclerView.SCROLL_STATE_IDLE
                    ) {
                        Toast.makeText(context, "загрузка...", Toast.LENGTH_LONG).show()

                        App.pokemonListViewModel.getNextPokemons()
                    }
                }
            }
        )
    }

    private fun handleNetworkError() {
        requireActivity().runOnUiThread {
            Toast.makeText(requireActivity(), "Ошибка сети", Toast.LENGTH_LONG).show()
        }
    }
}