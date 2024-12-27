package com.example.kotlinapp.data.source

import androidx.lifecycle.map
import com.example.kotlinapp.data.source.local.favorites.FavoritePokemon
import com.example.kotlinapp.data.source.local.favorites.FavoritePokemonDao
import com.example.kotlinapp.data.source.remote.pokemons.PokemonApi
import com.example.kotlinapp.domain.pokemons.Pokemon
import com.example.kotlinapp.domain.pokemons.PokemonItem
import com.example.kotlinapp.domain.pokemons.PokemonRepository

class PokemonRepositoryImpl(
    private val pokemonApi: PokemonApi,
    private val favoritePokemonDao: FavoritePokemonDao
) : PokemonRepository {

    override fun getPokemonList(
        limit: Int,
        offset: Int
    ): List<PokemonItem> {
        val getPokemonNamesResponse = pokemonApi
            .getPokemonNames(
                limit = limit,
                offset
            )
            .execute()

        val pokemonList = getPokemonNamesResponse.body()!!.names.map { response ->
            val pokemonDescription = pokemonApi.getPokemon(response.name).execute().body()!!

            PokemonItem(
                id = pokemonDescription.id,
                name = response.name,
                smallSprite = pokemonDescription.sprites.frontDefault,
            )
        }
        return pokemonList
    }

    override fun getPokemonByName(name: String): Pokemon {
        val pokemonDescription = pokemonApi.getPokemon(name).execute().body()!!

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

        val pokemonRemote = with(pokemonDescription) {
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
                flavor = pokemonApi.getPokemonFlavor(name).execute()
                    .body()!!.flavorTextEntries[9].flavorText
                    .replace("\n", " ")
            )
        }

        return pokemonRemote
    }

    override fun changeFavorite(pokemonName: String, isFavorite: Boolean) {
        if (isFavorite) {
            favoritePokemonDao.deleteByName(pokemonName)
        } else {
            favoritePokemonDao.insert(FavoritePokemon(pokemonName))
        }
    }

    override fun getFavoriteNames() = favoritePokemonDao.getAll().map {
        it.map {
            it.name
        }
    }
}


