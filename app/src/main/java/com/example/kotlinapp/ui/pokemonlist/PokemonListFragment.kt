package com.example.kotlinapp.ui.pokemonlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.databinding.FragmentMainBinding
import com.example.kotlinapp.ui.pokemoninfo.PokemonInfo
import kotlinx.coroutines.launch

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                pokemonListViewModel.pokemonItemListFlow.collect {
                    adapter.setPokemons(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                pokemonListViewModel.nextPageLoadingStateFlow.collect {
                    it?.let {
                        if (it.isLoaded) {
                            showSuccessMessage()
                        } else {
                            handleNetworkError()
                            Log.d(
                                "next page loading failure",
                                it.error!!.message.toString()
                            )
                        }
                    }
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

