package com.example.kotlinapp.ui.pokemoninfo

import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.Pokemon

data class PokemonInfoScreenState(
    val pokemon: Pokemon? = null,
    val loadingState: LoadingState
)
