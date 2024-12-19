package com.example.kotlinapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val pokemonListViewModel = ViewModelProvider(this)[PokemonListViewModel::class.java]
        val pokemonListLiveData = pokemonListViewModel.pokemonItemListLiveData

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val adapter = PokemonAdapter(
            onPokemonClick = { pokemonItem: PokemonItem ->
                val navController = findNavController()
                navController.navigate(route = PokemonInfo(name = pokemonItem.name))
            },
            onIsFavoriteClick = { pokemonItem: PokemonItem ->
                pokemonListViewModel.toggleFavorite(pokemonItem)
            }
        )


        pokemonListLiveData.observe(viewLifecycleOwner) {
            adapter.setPokemons(it)
        }

        pokemonListViewModel.nextPageLoadingState.observe(viewLifecycleOwner) {
            with(it) {
                if (isLoaded) {
                    showSuccessMessage()
                } else {
                    handleNetworkError()
                    Log.d(
                        "next page loading failure",
                        error!!.message.toString()
                    )
                }
            }
        }

        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1) &&
                        newState == RecyclerView.SCROLL_STATE_IDLE
                    ) {
                        Toast.makeText(context, "загрузка...", Toast.LENGTH_LONG).show()

                        pokemonListViewModel.loadNextPage()
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

    private fun showSuccessMessage() {
        requireActivity().runOnUiThread {
            Toast.makeText(requireActivity(), "Загрузка завершена", Toast.LENGTH_LONG).show()
        }
    }
}

