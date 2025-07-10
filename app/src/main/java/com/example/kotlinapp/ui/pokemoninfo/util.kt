package com.example.kotlinapp.ui.pokemoninfo

import com.example.kotlinapp.R
import com.example.kotlinapp.data.BaseStat

fun get3digitValue(value: Int): String {
    return when (value) {
        in 0..9 -> "00${value}"
        in 10..99 -> "0${value}"
        else -> "$value"
    }
}

fun getColor(type: String): Int {
    return when (type) {
        "normal" -> R.color.normal
        "fire" -> R.color.fire
        "water" -> R.color.water
        "grass" -> R.color.grass
        "electric" -> R.color.electric
        "ice" -> R.color.ice
        "fighting" -> R.color.fighting
        "poison" -> R.color.poison
        "ground" -> R.color.ground
        "flying" -> R.color.flying
        "psychic" -> R.color.psychic
        "bug" -> R.color.bug
        "rock" -> R.color.rock
        "ghost" -> R.color.ghost
        "dark" -> R.color.dark
        "dragon" -> R.color.dragon
        "steel" -> R.color.steel
        "fairy" -> R.color.fairy
        "stellar" -> R.color.stellar
        "unknown" -> R.color.unknown
        else -> R.color.unknown
    }
}

fun getBaseStatProgress(baseStat: BaseStat) = { baseStat.baseStatValue.toFloat() / 233f }

val getPokemonHeightInMeters = { height: Int -> "${(height).toFloat() / 10} m" }

val getPokemonAbilitiesString = { abilities: List<String> ->
    abilities.joinToString(separator = "\n") { it.replaceFirstChar { it.uppercase() } }
}

fun getBaseStatList(hp: Int, attack: Int, defense: Int, speed: Int): List<BaseStat> {
    return listOf(
        BaseStat(
            baseStatName = "HP",
            baseStatStringValue = get3digitValue(value = hp),
            baseStatValue = hp
        ),
        BaseStat(
            baseStatName = "ATK",
            baseStatStringValue = get3digitValue(value = attack),
            baseStatValue = attack
        ),
        BaseStat(
            baseStatName = "DEF",
            baseStatStringValue = get3digitValue(value = defense),
            baseStatValue = defense
        ),
        BaseStat(
            baseStatName = "SPD",
            baseStatStringValue = get3digitValue(value = speed),
            baseStatValue = speed
        ),
    )
}