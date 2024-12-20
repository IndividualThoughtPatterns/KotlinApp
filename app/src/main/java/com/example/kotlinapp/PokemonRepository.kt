package com.example.kotlinapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PokemonRepository {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .readTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .build()

    private val baseURL = "https://pokeapi.co/api/v2/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiInterface = retrofit.create(ApiInterface::class.java)

    fun getPokemonList(
        limit: Int,
        offset: Int
    ): List<PokemonItem> {
        val getPokemonNamesResponse = apiInterface
            .getPokemonNames(
                limit = limit,
                offset
            )
            .execute()

        val pokemonList = getPokemonNamesResponse.body()!!.names.map { response ->
            val pokemonDescription = apiInterface.getPokemon(response.name).execute().body()!!

            PokemonItem(
                id = pokemonDescription.id,
                name = response.name,
                smallSprite = pokemonDescription.sprites.frontDefault,
            )
        }
        return pokemonList
    }

    fun getPokemonByName(name: String): Pokemon {
        val pokemonDescription = apiInterface.getPokemon(name).execute().body()!!

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

        val pokemon = with(pokemonDescription) {
            Pokemon(
                id = id,
                name = name,
                smallSprite = sprites.frontDefault,
                bigSprite = sprites.other.officialArtwork.frontDefault,
                types = pokemonTypeNames,
                abilities = pokemonAbilityNames,
                height = height,
                weight = weight,
                hp = baseStats[0].toInt(),
                defense = baseStats[2].toInt(),
                attack = baseStats[1].toInt(),
                speed = baseStats[5].toInt(),
                flavor = apiInterface.getPokemonFlavor(name).execute()
                    .body()!!.flavorTextEntries[9].flavorText
                    .replace("\n", " ")
            )
        }

        return pokemon
    }

    class PokemonItem(
        val id: Int,
        val name: String,
        val smallSprite: String
    )
}


