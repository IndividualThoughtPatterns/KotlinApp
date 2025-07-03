package com.example.kotlinapp.domain

import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.Pokemon
import com.example.kotlinapp.data.source.PokemonRepository
import java.io.IOException

class GetPokemonByNameUseCase(
    private val pokemonRepository: PokemonRepository,
    private val pokemonName: String
) {
    private var pokemon: Pokemon? = null
    private var loadingState: LoadingState? = null

    suspend operator fun invoke(): PokemonWithLoadingState {
        try {
            pokemon = pokemonRepository.getPokemonByName(pokemonName)

            loadingState = LoadingState(
                isLoaded = true,
                error = null
            )
        } catch (e: IOException) {
            loadingState = LoadingState(
                isLoaded = false,
                error = e
            )
        }
        return PokemonWithLoadingState(
            pokemon,
            loadingState!!
        )
    }

    data class PokemonWithLoadingState(
        val pokemon: Pokemon?,
        val loadingState: LoadingState
    )
}