package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.data.LoadingStateEnum
import com.example.kotlinapp.data.Pokemon

val LocalPokemon = staticCompositionLocalOf<Pokemon> {
    error("pokemon not present")
}

@Composable
fun PokemonInfoScreen(name: String) {
    val pokemonInfoViewModel =
        viewModel<PokemonInfoViewModel>(factory = PokemonInfoViewModelFactory(name))
    val loadingState = pokemonInfoViewModel.loadingState.collectAsState()
    var pokemon = pokemonInfoViewModel.pokemonStateFlow.collectAsState()

    when (loadingState.value) {
        null, LoadingStateEnum.STARTED -> PokemonInfoLoading()
        LoadingStateEnum.SUCCESS -> {
            CompositionLocalProvider(
                LocalPokemon provides pokemon.value!!
            ) {
                PokemonInfoContent()
            }
        }

        LoadingStateEnum.FAILED -> PokemonInfoError(pokemonInfoViewModel = pokemonInfoViewModel)
    }
}