package com.example.kotlinapp.ui.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.kotlinapp.App
import com.example.kotlinapp.data.FavoritePokemon
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.ui.CommandFlow
import com.example.kotlinapp.ui.emit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PokemonListViewModel : ViewModel() {
    private var favoritePokemonDao = App.instance.db.favoritePokemonDao()
    private val pokemonRepository = App.instance.pokemonRepository

    private var _pokemonItemListFlow = pokemonRepository.getPokemonListPagingFlow()
        .cachedIn(viewModelScope)
        .combine(favoritePokemonDao.getAllAsFlow()) { pagingData, favorites ->
            pagingData.map { pokemonItemWithId ->
                PokemonItem(
                    sprite = pokemonItemWithId.smallSprite,
                    name = pokemonItemWithId.name,
                    isFavorite = favorites
                        .firstOrNull { it.name == pokemonItemWithId.name } != null
                )
            }
        }

    val pokemonItemListFlow: Flow<PagingData<PokemonItem>> = _pokemonItemListFlow

    val commandFlow = CommandFlow<PokemonListScreenUiCommand>(viewModelScope)

    fun onEvent(event: PokemonListEvent) {
        when (event) {
            is PokemonListEvent.OnToggleFavoriteClick -> {
                toggleFavorite(event.pokemonItem)
            }

            is PokemonListEvent.OnPokemonItemClick -> {
                commandFlow emit PokemonListScreenUiCommand.NavigateToPokemonInfo(name = event.name)
            }

            is PokemonListEvent.OnError -> {
                commandFlow emit PokemonListScreenUiCommand.ShowErrorMessage(
                    message = ""
                )
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
}