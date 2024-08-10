package com.example.kotlinapp

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class PokemonsNetwork {
    private val client = OkHttpClient()
    val gson = Gson()

    fun getPokemonNames(
        limit: Int, onResult: (List<String>) -> Unit, onError: () -> Unit
    ) {

        val url = "https://pokeapi.co/api/v2/pokemon?limit=$limit"
        val request = Request.Builder().url(url).build()

        Thread {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val pokemonNames =
                            gson.fromJson(response.body!!.string(), PokemonNames::class.java)
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
        name: String, onResult: (Pokemon) -> Unit, onError: () -> Unit
    ) {

        val url = "https://pokeapi.co/api/v2/pokemon/$name"
        val request = Request.Builder().url(url).build()

        Thread {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val data = response.body!!.string()

                        val json = gson.fromJson(data, PokemonJSONInfo::class.java)!!
                        val frontDefault = json.sprites.frontDefault

                        val pokemonTypes = json.types
                        val pokemonTypenames = MutableList(pokemonTypes.size) {
                            pokemonTypes[it].type.name
                        }

                        val pokemonAbilities = json.abilities
                        val pokemonAbilityNames = MutableList(pokemonAbilities.size) {
                            pokemonAbilities[it].ability.name
                        }

                        val height = json.height.toString()
                        val weight = json.weight.toString()

                        val pokemonStats = json.stats
                        val baseStats = MutableList(pokemonStats.size) {
                            pokemonStats[it].baseStat.toString()
                        }

                        val pokemon = Pokemon(
                            name,
                            frontDefault,
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
        limit: Int, onResult: (List<Pokemon>) -> Unit, onError: () -> Unit
    ) {
        getPokemonNames(limit, onResult = { pokemonNames ->
            var pokemons: MutableList<Pokemon> = mutableListOf<Pokemon>()
            var counter = 1

            pokemonNames.forEach {
                PokemonsNetwork().getPokemon(it, onResult = { pokemon ->
                    pokemons.add(pokemon)
                    counter++

                    if (counter == pokemonNames.size) {
                        onResult(pokemons)
                    }
                }, onError = {
                    onError()
                })
            }
        }, onError = {
            onError()
        })
    }

    class PokemonNames(
        @SerializedName("results") val names: List<Name>
    ) {
        class Name(
            @SerializedName("name") val name: String
        )
    }

    class PokemonJSONInfo(
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
