package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PokemonInfoScreen(name: String) {
    val pokemonInfoViewModel = viewModel { PokemonInfoViewModel(name) }
    val state by pokemonInfoViewModel.state.collectAsState()

    PokemonInfoContent(
        state = state,
        onEvent = pokemonInfoViewModel::onEvent
    )
}