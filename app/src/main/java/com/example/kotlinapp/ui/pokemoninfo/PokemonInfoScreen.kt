package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.Pokemon
import com.example.kotlinapp.ui.PokemonLoadingScreen

val LocalPokemon = staticCompositionLocalOf<Pokemon> {
    error("pokemon not present")
}

@Composable
fun PokemonInfoScreen(modifier: Modifier, name: String) {
    val pokemonInfoViewModel =
        viewModel<PokemonInfoViewModel>(factory = PokemonInfoViewModelFactory(name))
    val loadingState = pokemonInfoViewModel.loadingStateFlow.collectAsState()
    var pokemonState = pokemonInfoViewModel.pokemonStateFlow.collectAsState()

    when (loadingState.value) {
        null, LoadingState.STARTED -> PokemonLoadingScreen(modifier = Modifier)
        LoadingState.SUCCESS -> {
            CompositionLocalProvider(
                LocalPokemon provides pokemonState.value!!
            ) {
                PokemonInfoContent(modifier = Modifier)
            }
        }

        LoadingState.FAILED -> PokemonInfoError(
            modifier = Modifier,
            pokemonInfoViewModel = pokemonInfoViewModel
        )
    }
}