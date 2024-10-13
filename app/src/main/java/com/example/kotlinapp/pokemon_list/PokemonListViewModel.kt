package com.example.kotlinapp.pokemon_list

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.kotlinapp.App
import com.example.kotlinapp.Pokemon
import com.example.kotlinapp.R
import com.example.kotlinapp.pokemon_database.FavoritePokemon
import com.example.kotlinapp.pokemon_info.PokemonInfoFragment
import java.io.IOException
import java.util.concurrent.Executors

class PokemonListViewModel(
    private val fragment: PokemonListFragment,
    val handleNetworkError: () -> Unit
) : ViewModel() {
    private val limit = 20
    private var offsetFactor = 0
    private var offset = limit * offsetFactor
    private var pokemonListLiveData = MutableLiveData<List<Pokemon>>()
    private val executor = Executors.newSingleThreadExecutor()
    private var favoritePokemonDao = App.db.favoritePokemonDao()
    private var favoritePokemonListLiveData = favoritePokemonDao.getAll()
    private val pokemonNetwork = App.instance.pokemonNetwork

    val adapter = PokemonAdapter(
        onPokemonClick = { pokemonItem: PokemonItem ->
            val bundle = Bundle()

            val pokemon = pokemonListLiveData.value!!.firstOrNull { it.name == pokemonItem.name }

            bundle.putSerializable("pokemon", pokemon)

            fragment.parentFragmentManager.beginTransaction()
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

    init {
        setPokemons()
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

    private fun setPokemons() {
        val favoritePokemonListObserver = Observer<List<FavoritePokemon>> {
            adapter.setPokemons(
                buildPokemonItems(
                    pokemons = pokemonListLiveData.value ?: emptyList(),
                    favorites = it
                )
            )
        }

        favoritePokemonListLiveData
            .observe(fragment.viewLifecycleOwner, favoritePokemonListObserver)

        val pokemonListObserver = Observer<List<Pokemon>> {
            adapter.setPokemons(
                buildPokemonItems(
                    pokemons = it,
                    favorites = favoritePokemonListLiveData.value ?: emptyList()
                )
            )
        }

        pokemonListLiveData.observe(fragment.viewLifecycleOwner, pokemonListObserver)

        executor.submit {
            try {
                pokemonListLiveData.postValue(
                    pokemonNetwork.getPokemons(
                        limit = limit,
                        offset = offset
                    )
                )
            } catch (e: IOException) {
                handleNetworkError()
            }
        }
    }

    fun getNextPokemons() {
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