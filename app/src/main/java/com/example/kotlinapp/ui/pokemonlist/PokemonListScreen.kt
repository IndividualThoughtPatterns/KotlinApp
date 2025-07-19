package com.example.kotlinapp.ui.pokemonlist

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.kotlinapp.ui.LocalNavController
import com.example.kotlinapp.ui.NavRoutes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen() {
    val pokemonListViewModel = viewModel { PokemonListViewModel() }
    val state = pokemonListViewModel.pokemonListFlow.collectAsLazyPagingItems()
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

                    is PokemonListScreenUiCommand.NavigateToPokemonSettings -> {
                        navController.navigate(NavRoutes.PokemonSettings)
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "PokemonList") },
                actions = {
                    IconButton(
                        onClick = {
                            pokemonListViewModel.onEvent(event = PokemonListEvent.OnSettingsClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Настройки"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.LightGray,
                    actionIconContentColor = Color.LightGray
                )
            )
        }
//        floatingActionButton = {
//            FloatingActionButton(
//                modifier = Modifier
//                    .padding(end = 60.dp, bottom = 50.dp),
//                onClick = {
//                    pokemonListViewModel.onEvent(event = PokemonListEvent.OnSettingsClick)
//                }
//            ) {
//                Icon(
//                    modifier = Modifier.size(28.dp),
//                    imageVector = Icons.Outlined.Settings,
//                    contentDescription = "Настройки"
//                )
//            }
//        },
    ) { padding ->
        PokemonListContent(
            state = state,
            onEvent = pokemonListViewModel::onEvent,
            modifier = Modifier.padding(padding)
        )
    }
}