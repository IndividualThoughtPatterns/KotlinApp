package com.example.kotlinapp

import android.util.Log
import java.io.IOException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class PokemonsNetwork {
    private val client = OkHttpClient()

    fun getPokemons(
        limit: Int,
        onResult: (List<Pokemon>) -> Unit,
        onError: () -> Unit
    ) {
        lateinit var pokemons: List<Pokemon>

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

                    pokemons = names.map { Pokemon(name = it) }
                    onResult(pokemons)
                } else onError()
            }
        })
    }
}