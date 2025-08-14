package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PokemonInfoScreen(name: String) {
    val pokemonInfoViewModel =
        koinViewModel<PokemonInfoViewModel>(parameters = { parametersOf(name) })
    val state by pokemonInfoViewModel.state.collectAsState()

    PokemonInfoContent(
        state = state,
        onEvent = pokemonInfoViewModel::onEvent
    )
}