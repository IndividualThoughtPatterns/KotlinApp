package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.data.Pokemon

val LocalPokemon = staticCompositionLocalOf<Pokemon> {
    error("pokemon not present")
}

@Composable
fun PokemonInfoScreen(name: String) {
    val pokemonInfoViewModel =
        viewModel<PokemonInfoViewModel>(factory = PokemonInfoViewModelFactory(name))
    val state = pokemonInfoViewModel.state.collectAsState()

    PokemonInfoContent(
        state = state.value,
        onEvent = pokemonInfoViewModel::onEvent,
        modifier = Modifier
    )
}