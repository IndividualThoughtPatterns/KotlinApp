package com.example.kotlinapp.ui.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.kotlinapp.App
import com.example.kotlinapp.data.FavoritePokemonEntity
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.data.source.PokemonRemoteMediator
import com.example.kotlinapp.ui.CommandFlow
import com.example.kotlinapp.ui.emit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PokemonListViewModel : ViewModel() {
    private val favoritePokemonDao = App.instance.db.favoritePokemonDao()
    private val pokemonItemWithIdEntityDao = App.instance.db.pokemonItemWithIdDao()
    private val pokemonRepository = App.instance.pokemonRepository

    @OptIn(ExperimentalPagingApi::class)
    private val _pokemonListFlow = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 1,
            initialLoadSize = 20,
            enablePlaceholders = false
        ),
        remoteMediator = PokemonRemoteMediator(pokemonRepository, App.instance.db)
    ) {
        pokemonItemWithIdEntityDao.pagingSource()
    }
        .flow
        .cachedIn(viewModelScope)
        .combine(favoritePokemonDao.getAllAsFlow()) { pagingData, favorites ->
            pagingData.map { pokemonItemWithId ->
                PokemonItem(
                    sprite = pokemonItemWithId.sprite,
                    name = pokemonItemWithId.name,
                    isFavorite = favorites
                        .firstOrNull { it.name == pokemonItemWithId.name } != null,
                )
            }
        }

//    private val _pokemonListFlow: Flow<PagingData<PokemonItem>> = Pager(
//        config = PagingConfig(pageSize = 20, prefetchDistance = 1, initialLoadSize = 20)
//    ) {
//        PokemonPagingSource(pokemonRepository = pokemonRepository)
//    }
//        .flow
//        .cachedIn(viewModelScope)
//        .combine(favoritePokemonDao.getAllAsFlow()) { pagingData, favorites ->
//            pagingData.map { pokemonItemWithId ->
//                PokemonItem(
//                    sprite = pokemonItemWithId.smallSprite,
//                    name = pokemonItemWithId.name,
//                    isFavorite = favorites
//                        .firstOrNull { it.name == pokemonItemWithId.name } != null
//                )
//            }
//        }

    val pokemonListFlow: Flow<PagingData<PokemonItem>> = _pokemonListFlow

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
                favoritePokemonDao.insert(FavoritePokemonEntity(pokemonItem.name))
            }
        }
    }
}