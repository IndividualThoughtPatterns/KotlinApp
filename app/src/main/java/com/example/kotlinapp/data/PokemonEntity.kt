package com.example.kotlinapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "pokemons")
@TypeConverters(Converters::class)
data class PokemonEntity(
    @PrimaryKey()
    val id: Int,
    val name: String,
    val smallSprite: String,
    val bigSprite: String,
    val types: List<String>,
    val abilities: List<String>,
    val height: Int,
    val weight: Int,
    val hp: Int,
    val defense: Int,
    val attack: Int,
    val speed: Int,
    val flavor: String
)

fun PokemonEntity.toPokemon(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        smallSprite = smallSprite,
        bigSprite = bigSprite,
        types = types,
        abilities = abilities,
        height = height,
        weight = weight,
        hp = hp,
        defense = defense,
        attack = attack,
        speed = speed,
        flavor = flavor
    )
}

fun Pokemon.toPokemonEntity(): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        smallSprite = smallSprite,
        bigSprite = bigSprite,
        types = types,
        abilities = abilities,
        height = height,
        weight = weight,
        hp = hp,
        defense = defense,
        attack = attack,
        speed = speed,
        flavor = flavor
    )
}

class Converters {
    @TypeConverter
    fun fromString(string: String): List<String> {
        return string.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}
