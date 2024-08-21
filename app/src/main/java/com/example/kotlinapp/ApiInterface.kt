package com.example.kotlinapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("pokemon")
    fun getPokemonNames(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<Pokemon.PokemonNames>

    @GET("pokemon/{name}")
    fun getPokemon(
        @Path("name") name: String
    ): Call<Pokemon.PokemonDescription>
}