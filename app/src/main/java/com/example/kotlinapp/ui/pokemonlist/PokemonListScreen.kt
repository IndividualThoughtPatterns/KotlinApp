package com.example.kotlinapp.ui.pokemonlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PokemonListScreen() {
    val pokemonListViewModel = viewModel<PokemonListViewModel>()
    var state = pokemonListViewModel.state.collectAsState()

    PokemonListContent(
        state = state.value,
        onEvent = pokemonListViewModel::onEvent,
        modifier = Modifier
    )
}