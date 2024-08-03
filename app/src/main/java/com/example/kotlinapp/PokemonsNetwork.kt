package com.example.kotlinapp

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class PokemonsNetwork {
    private val client = OkHttpClient()

    fun getPokemonNames(
        limit: Int,
        onResult: (List<String>) -> Unit,
        onError: () -> Unit
    ) {

        val url = "https://pokeapi.co/api/v2/pokemon?limit=$limit"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onError()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val json = JSONObject(response.body!!.string())
                    val results = json.getJSONArray("results")

                    val names = List(results.length()) {
                        results.getJSONObject(it).getString("name")
                    }

                    val pokemons = names
                    onResult(pokemons)
                } else onError()
            }
        })
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

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onError()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val data = response.body!!.string()
                    val json = JSONObject(data)

                    val front_default = json.getJSONObject("sprites")
                        .getString("front_default")

                    val types = json.getJSONArray("types")
                    val typeNames: MutableList<String> = mutableListOf<String>()

                    for (i in 0 until types.length()) {
                        typeNames.add(
                            types.getJSONObject(i)
                                .getJSONObject("type")
                                .getString("name")
                        )
                    }

                    val abilities = json.getJSONArray("abilities")
                    val abilityNames: MutableList<String> = mutableListOf<String>()

                    for (i in 0 until abilities.length()) {
                        abilityNames.add(
                            abilities.getJSONObject(i)
                                .getJSONObject("ability")
                                .getString("name")
                        )
                    }

                    val height = json.getString("height").toInt().toString()
                    val weight = json.getString("weight").toInt().toString()

                    val stats = json.getJSONArray("stats")
                    val baseStats: MutableList<String> = mutableListOf<String>()

                    for (i in 0 until stats.length()) {
                        baseStats.add(
                            stats.getJSONObject(i)
                                .getInt("base_stat")
                                .toString()
                        )
                    }

                    val pokemon = Pokemon(
                        name,
                        front_default,
                        typeNames,
                        abilityNames,
                        height,
                        weight,
                        baseStats.get(0),
                        baseStats.get(2),
                        baseStats.get(1),
                        baseStats.get(5)
                    )

                    onResult(pokemon)
                } else onError()
            }
        })
    }
}