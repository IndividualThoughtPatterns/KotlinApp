package com.example.kotlinapp

import android.util.Log
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    class PokemonNames(
        @SerializedName("results") val names: List<Name>
    ) {
        class Name(
            @SerializedName("name") val name: String
        )
    }

    class PokemonDescription (
        @SerializedName("height") val height: Int,
        @SerializedName("weight") val weight: Int,
        @SerializedName("abilities") val abilities: List<AbilityObj>,
        @SerializedName("sprites") val sprites: FrontDefault,
        @SerializedName("types") val types: List<TypeObj>,
        @SerializedName("stats") val stats: List<Stat>
    ) {
        class AbilityObj(
            @SerializedName("ability") val ability: Ability
        ) {
            class Ability(
                @SerializedName("name") val name: String
            )
        }

        class TypeObj(
            @SerializedName("type") val type: Type
        ) {
            class Type(
                @SerializedName("name") val name: String
            )
        }

        class Stat(
            @SerializedName("base_stat") val baseStat: Int
        )

        class FrontDefault(
            @SerializedName("front_default") val frontDefault: String
        )
    }
}

interface ApiInterface {
    @GET("pokemon")
    fun getPokemonNames(
        @Query("limit") limit: Int
    ): Call<PokemonsNetwork.PokemonNames>

    @GET("pokemon/{name}")
    fun getPokemon(
        @Path("name") name: String
    ): Call<PokemonsNetwork.PokemonDescription>
}
