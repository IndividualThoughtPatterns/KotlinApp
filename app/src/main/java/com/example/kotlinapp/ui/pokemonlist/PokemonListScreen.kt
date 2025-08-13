package com.example.kotlinapp.ui.pokemonlist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.kotlinapp.ui.LocalNavController
import com.example.kotlinapp.ui.NavRoutes
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokemonListScreen() {
    val pokemonListViewModel = koinViewModel<PokemonListViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = LocalNavController.current

    LaunchedEffect(key1 = Unit) {
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        PokemonListContent(
            state = PokemonListScreenState(
                pokemonItemListPaging = pokemonListViewModel.pokemonItemListFlow
            ),
            onEvent = pokemonListViewModel::onEvent,
            modifier = Modifier.padding(padding)
        )
    }
}