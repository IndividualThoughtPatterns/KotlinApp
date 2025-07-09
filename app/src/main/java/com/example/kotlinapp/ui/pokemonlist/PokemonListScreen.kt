package com.example.kotlinapp.ui.pokemonlist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.data.LoadingState

@Composable
fun PokemonListScreen() {
    val pokemonListViewModel = viewModel<PokemonListViewModel>()
    val state by pokemonListViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.loadingState is LoadingState.Error) {
        if (state.loadingState is LoadingState.Error) {
            snackbarHostState.showSnackbar(
                message = "Ошибка сети",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        PokemonListContent(
            state = state,
            onEvent = pokemonListViewModel::onEvent,
            modifier = Modifier.padding(padding)
        )
    }
}