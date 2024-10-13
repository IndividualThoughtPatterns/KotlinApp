package com.example.kotlinapp.pokemon_network

import com.example.kotlinapp.Pokemon
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonNetwork {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val baseURL = "https://pokeapi.co/api/v2/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiInterface = retrofit.create(ApiInterface::class.java)

    fun getPokemons(
        limit: Int,
        offset: Int
    ): List<Pokemon> {
        val getPokemonNamesResponse = apiInterface
            .getPokemonNames(
                limit = limit,
                offset
            )
            .execute()

        var pokemons = getPokemonNamesResponse.body()!!.names.map { response ->
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
                id = pokemonDescription.id,
                name = response.name,
                smallSprite = pokemonDescription.sprites.frontDefault,
                bigSprite = pokemonDescription.sprites.other.officialArtwork.frontDefault,
                types = pokemonTypeNames,
                abilities = pokemonAbilityNames,
                height = pokemonDescription.height,
                weight = pokemonDescription.weight,
                hp = baseStats[0].toInt(),
                defense = baseStats[2].toInt(),
                attack = baseStats[1].toInt(),
                speed = baseStats[5].toInt(),
                flavor = ""
            )
        }
        pokemons = pokemons.map { it ->
            var pokemonFlavor = apiInterface.getPokemonFlavor(it.name).execute()
                .body()!!.flavorTextEntries[9].flavorText
            pokemonFlavor = pokemonFlavor.replace("\n", " ")

            Pokemon(
                id = it.id,
                name = it.name,
                smallSprite = it.smallSprite,
                bigSprite = it.bigSprite,
                types = it.types,
                abilities = it.abilities,
                height = it.height,
                weight = it.weight,
                hp = it.hp,
                defense = it.defense,
                attack = it.attack,
                speed = it.speed,
                flavor = pokemonFlavor
            )
        }
        return pokemons
    }
}


