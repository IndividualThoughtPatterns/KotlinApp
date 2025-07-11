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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.ui.LocalNavController
import com.example.kotlinapp.ui.NavRoutes
import kotlinx.coroutines.launch

@Composable
fun PokemonListScreen() {
    val pokemonListViewModel = viewModel { PokemonListViewModel() }
    val state by pokemonListViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = LocalNavController.current

    LaunchedEffect(key1 = Unit) {
        pokemonListViewModel.viewModelScope.launch {
            pokemonListViewModel.commandFlow.collectSafely {
                when (it) {
                    is PokemonListScreenUiCommand.ShowErrorMessage -> {
                        snackbarHostState.showSnackbar(
                            message = "Ошибка сети",
                            duration = SnackbarDuration.Short
                        )
                    }
                    is PokemonListScreenUiCommand.NavigateToPokemonInfo -> {
                        navController.navigate(NavRoutes.PokemonInfo(name = it.name))
                    }
                }
            }
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