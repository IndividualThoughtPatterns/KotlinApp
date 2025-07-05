package com.example.kotlinapp.ui.pokemonlist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.ui.PokemonLoadingScreen

@Composable
fun PokemonListScreen(modifier: Modifier) {
    val pokemonListViewModel = viewModel<PokemonListViewModel>()
    var pokemonItemsListState = pokemonListViewModel.pokemonItemListFlow.collectAsState()
    var nextPageLoadingState = pokemonListViewModel.nextPageLoadingStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val lazyColumnState = rememberLazyListState()
    LaunchedEffect(lazyColumnState) {
        snapshotFlow {
            !lazyColumnState.canScrollForward &&
                    (lazyColumnState.layoutInfo.visibleItemsInfo.isNotEmpty())
        }.collect {
            if (!lazyColumnState.canScrollForward &&
                (lazyColumnState.layoutInfo.visibleItemsInfo.isNotEmpty())
            ) {
                pokemonListViewModel.loadNextPage()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            state = lazyColumnState
        ) {
            items(items = pokemonItemsListState.value) { pokemonItem ->
                PokemonElement(
                    modifier = Modifier,
                    pokemonItem = pokemonItem,
                    onIsFavoriteClick = { pokemonItem: PokemonItem ->
                        pokemonListViewModel.toggleFavorite(pokemonItem)
                    }
                )
            }
        }

        when (nextPageLoadingState.value) {
            null, LoadingState.STARTED -> {
                PokemonLoadingScreen(modifier = Modifier)
            }

            LoadingState.SUCCESS -> {}
            LoadingState.FAILED -> {
                LaunchedEffect(nextPageLoadingState) {
                    snackbarHostState.showSnackbar(
                        message = "Ошибка сети",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}