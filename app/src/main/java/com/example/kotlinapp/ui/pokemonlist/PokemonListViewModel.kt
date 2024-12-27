package com.example.kotlinapp.ui.pokemonlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.kotlinapp.domain.pokemons.PokemonItem
import com.example.kotlinapp.domain.pokemons.PokemonRepository
import com.example.kotlinapp.ui.LoadingState
import java.io.IOException
import java.util.concurrent.Executors

class PokemonListViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val executor = Executors.newSingleThreadExecutor()
    private val limit = 20
    private var offsetFactor = 0
    private var offset = limit * offsetFactor

    private val _pokemonItemListLiveData = MutableLiveData<List<PokemonAdapter.PokemonItem>>()
    val pokemonItemListLiveData: LiveData<List<PokemonAdapter.PokemonItem>> =
        _pokemonItemListLiveData

    private val _nextPageLoadingState = MutableLiveData<LoadingState>()
    val nextPageLoadingState = _nextPageLoadingState

    private val favoritePokemonListLiveData = pokemonRepository.getFavoriteNames()
    private val favoritePokemonListObserver = Observer<List<String>> {
        _pokemonItemListLiveData.postValue(
            buildPokemonItems(
                pokemonList = pokemonListLiveData.value ?: emptyList(),
                favorites = it
            )
        )
    }

    private val pokemonListLiveData = MutableLiveData<List<PokemonItem>>()
    private val pokemonListObserver = Observer<List<PokemonItem>> {
        _pokemonItemListLiveData.postValue(
            buildPokemonItems(
                pokemonList = it,
                favorites = favoritePokemonListLiveData.value ?: emptyList()
            )
        )
    }

    fun loadNextPage() {
        executor.submit {
            try {
                val prevList = pokemonListLiveData.value ?: emptyList()
                pokemonListLiveData.postValue(
                    prevList + pokemonRepository.getPokemonList(
                        limit = limit,
                        offset = offset
                    )
                )
                _nextPageLoadingState.postValue(
                    LoadingState(
                        isLoaded = true,
                        error = null
                    )
                )
                offsetFactor++
                offset = limit * offsetFactor
            } catch (e: IOException) {
                _nextPageLoadingState.postValue(
                    LoadingState(
                        isLoaded = false,
                        error = e
                    )
                )
            }
        }
    }

    fun toggleFavorite(pokemonItem: PokemonAdapter.PokemonItem) {
        executor.submit {
            pokemonRepository.changeFavorite(pokemonItem.name, pokemonItem.isFavorite)
        }
    }

    private fun buildPokemonItems(
        pokemonList: List<PokemonItem>,
        favorites: List<String>
    ) = pokemonList.map { pokemonItem ->
        PokemonAdapter.PokemonItem(
            sprite = pokemonItem.smallSprite,
            name = pokemonItem.name,
            isFavorite = favorites.firstOrNull { it == pokemonItem.name } != null
        )
    }

    init {
        favoritePokemonListLiveData.observeForever(favoritePokemonListObserver)
        pokemonListLiveData.observeForever(pokemonListObserver)
        loadNextPage()
    }

    override fun onCleared() {
        super.onCleared()
        favoritePokemonListLiveData.removeObserver(favoritePokemonListObserver)
        pokemonListLiveData.removeObserver(pokemonListObserver)
    }
}