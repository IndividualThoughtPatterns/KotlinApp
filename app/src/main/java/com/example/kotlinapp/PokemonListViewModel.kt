package com.example.kotlinapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import java.io.IOException
import java.util.concurrent.Executors

class PokemonListViewModel : ViewModel() {
    private val executor = Executors.newSingleThreadExecutor()
    private var favoritePokemonDao = App.instance.db.favoritePokemonDao()
    private val pokemonRepository = App.instance.pokemonRepository
    private val limit = 20
    private var offsetFactor = 0
    private var offset = limit * offsetFactor

    private val _pokemonItemListLiveData = MutableLiveData<List<PokemonItem>>()
    val pokemonItemListLiveData: LiveData<List<PokemonItem>> = _pokemonItemListLiveData

    private val _nextPageLoadingState = MutableLiveData<LoadingState>()
    val nextPageLoadingState = _nextPageLoadingState

    private val favoritePokemonListLiveData = favoritePokemonDao.getAll()
    private val favoritePokemonListObserver = Observer<List<FavoritePokemon>> {
        _pokemonItemListLiveData.postValue(
            buildPokemonItems(
                pokemonList = pokemonListLiveData.value ?: emptyList(),
                favorites = it
            )
        )
    }

    private val pokemonListLiveData = MutableLiveData<List<PokemonRepository.PokemonItem>>()
    private val pokemonListObserver = Observer<List<PokemonRepository.PokemonItem>> {
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

    fun toggleFavorite(pokemonItem: PokemonItem) {
        executor.submit {
            if (pokemonItem.isFavorite) {
                favoritePokemonDao.deleteByName(pokemonItem.name)
            } else {
                favoritePokemonDao.insert(FavoritePokemon(pokemonItem.name))
            }
        }
    }

    private fun buildPokemonItems(
        pokemonList: List<PokemonRepository.PokemonItem>,
        favorites: List<FavoritePokemon>
    ) = pokemonList.map { pokemonItem ->
        PokemonItem(
            sprite = pokemonItem.smallSprite,
            name = pokemonItem.name,
            isFavorite = favorites.firstOrNull { it.name == pokemonItem.name } != null
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