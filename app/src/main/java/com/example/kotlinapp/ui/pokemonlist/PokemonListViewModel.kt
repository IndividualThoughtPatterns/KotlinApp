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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class PokemonListViewModel : ViewModel() {
    private var favoritePokemonDao = App.instance.db.favoritePokemonDao()
    private val pokemonRepository = App.instance.pokemonRepository
    private val limit = 20
    private var offsetFactor = 0
    private var offset = limit * offsetFactor

    private val pokemonItemWithIdListFlow =
        MutableStateFlow<List<PokemonRepository.PokemonItemWithId>>(
            emptyList()
        )

    private val _state =
        MutableStateFlow<PokemonListScreenState>(PokemonListScreenState(LoadingState.Loading))
    val state = _state.asStateFlow()

    init {
        loadNextPage()
    }

    fun onEvent(event: PokemonListEvent) {
        when (event) {
            is PokemonListEvent.OnToggleFavoriteClick -> {
                toggleFavorite(event.pokemonItem)
            }

            PokemonListEvent.OnScrolledBottom -> {
                loadNextPage()
            }
        }
    }

    fun loadNextPage() {
        _state.update { PokemonListScreenState(LoadingState.Loading) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prevList = pokemonItemWithIdListFlow.value
                val newList =
                    prevList + pokemonRepository.getPokemonList(limit = limit, offset = offset)
                pokemonItemWithIdListFlow.value = newList

                combine(
                    pokemonItemWithIdListFlow,
                    favoritePokemonDao.getAllAsFlow(),
                ) { pokemonList, favorites ->
                    PokemonListScreenState(
                        loadingState = LoadingState.Loaded(
                            value = buildPokemonItems(
                                pokemonList,
                                favorites
                            )
                        )
                    )
                }.collect {
                    _state.value = it
                    offsetFactor++
                    offset = limit * offsetFactor
                }
            } catch (e: IOException) {
                e.printStackTrace()
                _state.update { PokemonListScreenState(LoadingState.Error(e)) }
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