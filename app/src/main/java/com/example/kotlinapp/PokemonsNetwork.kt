package com.example.kotlinapp

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonsNetwork {

    fun getPokemons(
        limit: Int,
        onResult: (List<Pokemon>) -> Unit,
        onError: () -> Unit
    ) {
        Thread {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val baseURL =  "https://pokeapi.co/api/v2/"
            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiInterface = retrofit.create(ApiInterface::class.java)

            val getPokemonNamesResponse = apiInterface
                .getPokemonNames(
                    limit = limit,
                )
                .execute()

            if (getPokemonNamesResponse.isSuccessful) {
                onResult(
                    getPokemonNamesResponse.body()!!.names.map { response ->
                        val pokemonDescription = apiInterface.getPokemon(response.name).execute().body()!!

                        val pokemonTypes = pokemonDescription.types
                        val pokemonTypeNames = MutableList(pokemonTypes.size) {
                            pokemonTypes[it].type.name
                        }

                        val pokemonAbilities = pokemonDescription.abilities
                        val pokemonAbilityNames = MutableList(pokemonAbilities.size) {
                            pokemonAbilities[it].ability.name
                        }

                        val pokemonStats = pokemonDescription.stats
                        val baseStats = MutableList(pokemonStats.size) {
                            pokemonStats[it].baseStat.toString()
                        }

                        Pokemon(
                            name = response.name,
                            sprite = pokemonDescription.sprites.frontDefault,
                            types = pokemonTypeNames,
                            abilities = pokemonAbilityNames,
                            height = pokemonDescription.height.toString(),
                            weight = pokemonDescription.weight.toString(),
                            hp = baseStats[0],
                            defense = baseStats[2],
                            attack = baseStats[1],
                            speed = baseStats[5]
                        )
                    }
                )
            } else onError()
       }.start()
    }
}


