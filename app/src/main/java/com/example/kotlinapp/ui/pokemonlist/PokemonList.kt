package com.example.kotlinapp.ui.pokemonlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.PokemonItem
import kotlinx.coroutines.launch

@Composable
fun PokemonList(
    navController: NavController // для проброски вроде был какой-то local че-то там вроде
) {
    val pokemonListViewModel = viewModel<PokemonListViewModel>()
    var pokemonItemsList = remember { mutableStateOf<List<PokemonItem>?>(null) }
    var nextPageLoadingState = remember { mutableStateOf<LoadingState?>(null) }

    val state = rememberLazyListState()
    LaunchedEffect(state) {
        snapshotFlow {
            !state.canScrollForward && (state.layoutInfo.visibleItemsInfo.isNotEmpty())
        }.collect {
            if (!state.canScrollForward && (state.layoutInfo.visibleItemsInfo.isNotEmpty())) {
                pokemonListViewModel.loadNextPage()
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            pokemonListViewModel.pokemonItemListFlow.collect {
                pokemonItemsList.value = it
            }
        }
    }


    if (pokemonItemsList.value != null) {
        LazyColumn(state = state) {
            items(items = pokemonItemsList.value!!) { pokemonItem ->
                PokemonElement(
                    pokemonItem = pokemonItem,
                    navController = navController,
                    onIsFavoriteClick = { pokemonItem: PokemonItem ->
                        pokemonListViewModel.toggleFavorite(pokemonItem)
                    }
                )
            }
        }
    }
}