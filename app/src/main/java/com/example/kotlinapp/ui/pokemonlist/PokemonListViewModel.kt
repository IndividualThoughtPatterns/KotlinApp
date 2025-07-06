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

    private val nextPageLoadingStateFlow =
        MutableStateFlow<LoadingState>(LoadingState.STARTED)

    private val pokemonItemWithIdListFlow =
        MutableStateFlow<List<PokemonRepository.PokemonItemWithId>>(
            emptyList()
        )

    val state = combine(
        pokemonItemWithIdListFlow,
        favoritePokemonDao.getAllAsFlow(),
        nextPageLoadingStateFlow
    ) { pokemonList, favorites, loadingState ->
        PokemonListScreenState(
            pokemonItemList = buildPokemonItems(pokemonList, favorites),
            loadingState = loadingState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PokemonListScreenState(loadingState = LoadingState.STARTED)
    )

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
        nextPageLoadingStateFlow.update { LoadingState.STARTED }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prevList = pokemonItemWithIdListFlow.value
                val newList =
                    prevList + pokemonRepository.getPokemonList(limit = limit, offset = offset)
                pokemonItemWithIdListFlow.value = newList

                nextPageLoadingStateFlow.update { LoadingState.SUCCESS }
                offsetFactor++
                offset = limit * offsetFactor
            } catch (e: IOException) {
                e.printStackTrace()
                nextPageLoadingStateFlow.update { LoadingState.FAILED }
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