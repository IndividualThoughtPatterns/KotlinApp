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
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.ui.PokemonLoadingScreen

@Composable
fun PokemonListContent(
    state: PokemonListScreenState,
    onEvent: (PokemonListEvent) -> Unit,
    modifier: Modifier
) {
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
                onEvent(PokemonListEvent.OnScrolledBottom)
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
            items(items = state.pokemonItemList) { pokemonItem ->
                PokemonElement(
                    modifier = Modifier,
                    pokemonItem = pokemonItem,
                    onToggleFavoriteClick = { pokemonItem: PokemonItem ->
                        onEvent(PokemonListEvent.OnToggleFavoriteClick(pokemonItem))
                    }
                )
            }
        }

        when (state.loadingState) {
            LoadingState.STARTED -> {
                PokemonLoadingScreen(modifier = Modifier)
            }

            LoadingState.SUCCESS -> {}
            LoadingState.FAILED -> {
                LaunchedEffect(state.loadingState) {
                    snackbarHostState.showSnackbar(
                        message = "Ошибка сети",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}