package com.example.kotlinapp.domain.pokemons

import androidx.lifecycle.LiveData

interface PokemonRepository {
    fun getPokemonList(
        limit: Int,
        offset: Int
    ): List<PokemonItem>

    fun getPokemonByName(name: String): Pokemon

    fun changeFavorite(pokemonName: String, isFavorite: Boolean)

    fun getFavoriteNames(): LiveData<List<String>>
}