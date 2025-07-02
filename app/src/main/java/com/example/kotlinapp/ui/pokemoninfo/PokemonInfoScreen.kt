package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.Pokemon
import kotlinx.coroutines.launch

@Composable
fun PokemonInfoScreen(name: String) {
    val pokemonInfoViewModel =
        viewModel<PokemonInfoViewModel>(factory = PokemonInfoViewModelFactory(name))
    var pokemon = remember { mutableStateOf<Pokemon?>(null) }
    var loadingState = remember { mutableStateOf<LoadingState?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            pokemonInfoViewModel.pokemonStateFlow.collect {
                if (it != null) {
                    pokemon.value = it
                }
            }
        }
        coroutineScope.launch {
            pokemonInfoViewModel.pokemonLoadingState.collect {
                if (it != null) {
                    loadingState.value = it
                }
            }
        }
    }

    if (pokemon.value == null && loadingState.value == null) { // надо переделать
        PokemonInfoLoading()
    } else if (loadingState.value!!.isLoaded) {
        PokemonInfoContent(pokemon = pokemon.value!!)
    } else if (!loadingState.value!!.isLoaded) {
        PokemonInfoError(pokemonInfoViewModel = pokemonInfoViewModel)
    }
}