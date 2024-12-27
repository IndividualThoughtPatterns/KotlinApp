package com.example.kotlinapp.data.source.remote.pokemons

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {
    @GET("pokemon")
    fun getPokemonNames(
        @Query("limit") limit: Int, @Query("offset") offset: Int
    ): Call<PokemonRemote.PokemonNames>

    @GET("pokemon/{name}")
    fun getPokemon(
        @Path("name") name: String
    ): Call<PokemonRemote.PokemonDescription>

    @GET("pokemon-species/{name}")
    fun getPokemonFlavor(
        @Path("name") name: String
    ): Call<PokemonRemote.PokemonFlavor>
}