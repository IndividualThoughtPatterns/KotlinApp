package com.example.kotlinapp

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class PokemonsNetwork {
    private val client = OkHttpClient()
    val gson = Gson()

    fun getPokemonNames(
        limit: Int,
        onResult: (List<String>) -> Unit,
        onError: () -> Unit
    ) {

        val url = "https://pokeapi.co/api/v2/pokemon?limit=$limit"
        val request = Request.Builder()
            .url(url)
            .build()

        Thread {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val pokemonNames = gson.fromJson(response.body!!.string(), PokemonNames::class.java)
                        val pokemons = List(pokemonNames.names.size) {
                            pokemonNames.names[it].name
                        }

                        onResult(pokemons)
                    } else onError()
                }
            } catch (e: IOException) {
                onError()
            }
        }.start()
    }

    fun getPokemon(
        name: String,
        onResult: (Pokemon) -> Unit,
        onError: () -> Unit
    ) {

        val url = "https://pokeapi.co/api/v2/pokemon/$name"
        val request = Request.Builder()
            .url(url)
            .build()

        Thread {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val data = response.body!!.string()

                        val pokemonSprites = gson.fromJson(data, PokemonSprites::class.java)
                        val front_default = pokemonSprites.sprites.frontDefault

                        val pokemonTypes = gson.fromJson(data, PokemonTypes::class.java)
                        val pokemonTypenames = MutableList(pokemonTypes.types.size) {
                            pokemonTypes.types[it].name
                        }

                        val pokemonAbilities = gson.fromJson(data, PokemonAbilities::class.java)
                        val pokemonAbilityNames = MutableList(pokemonAbilities.abilities.size) {
                            pokemonAbilities.abilities[it].name
                        }

                        val pokemonHW = gson.fromJson(data, PokemonHW::class.java)

                        val height = pokemonHW.height.toString()
                        val weight = pokemonHW.weight.toString()

                        val pokemonStats = gson.fromJson(data, PokemonStats::class.java)
                        val baseStats = MutableList(pokemonStats.stats.size) {
                            pokemonStats.stats[it].base_stat.toString()
                        }

                        val pokemon = Pokemon(
                            name,
                            front_default,
                            pokemonTypenames,
                            pokemonAbilityNames,
                            height,
                            weight,
                            hp = baseStats.get(0),
                            defense = baseStats.get(2),
                            attack = baseStats.get(1),
                            speed = baseStats.get(5)
                        )

                        onResult(pokemon)
                    } else onError()
                }
            } catch (e: IOException) {
                onError()
            }
        }.start()
    }

    fun getPokemons(
        limit: Int,
        onResult: (List<Pokemon>) -> Unit,
        onError: () -> Unit
    ) {
        getPokemonNames(
            limit,
            onResult = { pokemonNames ->
                var pokemons: MutableList<Pokemon> = mutableListOf<Pokemon>()
                var counter = 1

                pokemonNames.forEach {
                    PokemonsNetwork().getPokemon(
                        it,
                        onResult = { pokemon ->
                            pokemons.add(pokemon)
                            counter++

                            if (counter == pokemonNames.size) {
                                onResult(pokemons)
                            }
                        },
                        onError = {
                            onError()
                        }
                    )
                }
            },
            onError = {
                onError()
            }
        )
    }

    class PokemonNames (
        @SerializedName("results")
        val names: List<Name>
    ) {
        class Name (
            @SerializedName("name")
            val name:  String
        )
    }

    class PokemonAbilities (
        @SerializedName("abilities")
        val abilities: List<Ability>
    ) {
        class Ability (
            @SerializedName("name")
            val name: String
        )
    }

    class PokemonTypes (
        @SerializedName("types")
        val types: List<Type>
    ) {
        class Type (
            @SerializedName("name")
            val name: String
        )
    }

    class PokemonStats (
        @SerializedName("stats")
        val stats: List<Stat>
    ) {
        class Stat(
            @SerializedName("base_stat")
            val base_stat: Int
        )
    }

    class PokemonSprites (
        @SerializedName("sprites")
        val sprites: FrontDefault
    ) {
        class FrontDefault(
            @SerializedName("front_default")
            val frontDefault: String
        )
    }

    class PokemonHW (
        @SerializedName("height")
        val height: Int,
        @SerializedName("weight")
        val weight: Int
    )
}