package com.example.kotlinapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.FragmentMainBinding
import java.io.IOException
import java.util.concurrent.Executors

class PokemonListFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val executor = Executors.newSingleThreadExecutor()
    private var favoritePokemonDao = App.instance.db.favoritePokemonDao()
    private var favoritePokemonListLiveData = favoritePokemonDao.getAll()
    private val pokemonNetwork = App.instance.pokemonNetwork
    private val limit = 20
    private var offsetFactor = 0
    private var offset = limit * offsetFactor
    private var pokemonListLiveData = MutableLiveData<List<Pokemon>>()
    private val adapter = PokemonAdapter(
        onPokemonClick = { pokemonItem: PokemonItem ->
            val bundle = Bundle()

            val pokemon = pokemonListLiveData.value!!.firstOrNull { it.name == pokemonItem.name }

            bundle.putSerializable("pokemon", pokemon)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment, PokemonInfoFragment::class.java, bundle)
                .addToBackStack("PokemonListFragment").commit()
        },
        onIsFavoriteClick = { pokemonItem: PokemonItem ->
            executor.submit {
                if (pokemonItem.isFavorite) {
                    favoritePokemonDao.deleteByName(pokemonItem.name)
                } else {
                    favoritePokemonDao.insert(FavoritePokemon(pokemonItem.name))
                }
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        val favoritePokemonListObserver = Observer<List<FavoritePokemon>> {
            adapter.setPokemons(
                buildPokemonItems(
                    pokemons = pokemonListLiveData.value ?: emptyList(),
                    favorites = it
                )
            )
        }

        favoritePokemonListLiveData.observe(viewLifecycleOwner, favoritePokemonListObserver)

        val pokemonListObserver = Observer<List<Pokemon>> {
            adapter.setPokemons(
                buildPokemonItems(
                    pokemons = it,
                    favorites = favoritePokemonListLiveData.value ?: emptyList()
                )
            )
        }

        pokemonListLiveData.observe(viewLifecycleOwner, pokemonListObserver)

        executor.submit {
            try {
                pokemonListLiveData.postValue(pokemonNetwork.getPokemons(limit, offset))
            } catch (e: IOException) {
                handleNetworkError()
            }
        }

        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1) &&
                        newState == RecyclerView.SCROLL_STATE_IDLE
                    ) {
                        Toast.makeText(context, "загрузка...", Toast.LENGTH_LONG).show()

                        offsetFactor++
                        offset = limit * offsetFactor

                        executor.submit {
                            try {
                                val prevList = pokemonListLiveData.value ?: emptyList()
                                pokemonListLiveData.postValue(
                                    prevList + pokemonNetwork.getPokemons(
                                        limit,
                                        offset
                                    )
                                )
                            } catch (e: IOException) {
                                handleNetworkError()
                            }
                        }
                    }
                }
            }
        )
    }

    private fun buildPokemonItems(
        pokemons: List<Pokemon>,
        favorites: List<FavoritePokemon>
    ) = pokemons.map { pokemon ->
        PokemonItem(
            sprite = pokemon.smallSprite,
            name = pokemon.name,
            isFavorite = favorites.firstOrNull { it.name == pokemon.name } != null
        )
    }

    private fun handleNetworkError() {
        requireActivity().runOnUiThread {
            Toast.makeText(requireActivity(), "Ошибка сети", Toast.LENGTH_LONG).show()
        }
    }
}