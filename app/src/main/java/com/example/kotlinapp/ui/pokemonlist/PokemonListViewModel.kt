package com.example.kotlinapp.ui.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinapp.App
import com.example.kotlinapp.data.FavoritePokemon
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.data.source.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class PokemonListViewModel : ViewModel() {
    private var favoritePokemonDao = App.instance.db.favoritePokemonDao()
    private val pokemonRepository = App.instance.pokemonRepository
    private val limit = 20
    private var offsetFactor = 0
    private var offset = limit * offsetFactor

    private val _nextPageLoadingStateFlow = MutableStateFlow<LoadingState?>(null)
    val nextPageLoadingStateFlow = _nextPageLoadingStateFlow.asStateFlow()

    private val pokemonItemWithIdListFlow =
        MutableStateFlow<List<PokemonRepository.PokemonItemWithId>>(
            emptyList()
        )

    val pokemonItemListFlow = combine(
        pokemonItemWithIdListFlow,
        favoritePokemonDao.getAllAsFlow()
    ) { pokemonList, favorites ->
        buildPokemonItems(pokemonList, favorites)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prevList = pokemonItemWithIdListFlow.value
                val newList =
                    prevList + pokemonRepository.getPokemonList(limit = limit, offset = offset)
                pokemonItemWithIdListFlow.value = newList

                _nextPageLoadingStateFlow.update {
                    LoadingState(
                        isLoaded = true,
                        error = null
                    )
                }
                offsetFactor++
                offset = limit * offsetFactor
            } catch (e: IOException) {
                _nextPageLoadingStateFlow.update {
                    LoadingState(
                        isLoaded = false,
                        error = e
                    )
                }
            }
        }
    }

    fun toggleFavorite(pokemonItem: PokemonItem) {
        viewModelScope.launch(Dispatchers.IO) {
            if (pokemonItem.isFavorite) {
                favoritePokemonDao.deleteByName(pokemonItem.name)
            } else {
                favoritePokemonDao.insert(FavoritePokemon(pokemonItem.name))
            }
        }
    }

    private fun buildPokemonItems(
        pokemonList: List<PokemonRepository.PokemonItemWithId>,
        favorites: List<FavoritePokemon>
    ) = pokemonList.map { pokemonItemWithId ->
        PokemonItem(
            sprite = pokemonItemWithId.smallSprite,
            name = pokemonItemWithId.name,
            isFavorite = favorites.firstOrNull { it.name == pokemonItemWithId.name } != null
        )
    }
}