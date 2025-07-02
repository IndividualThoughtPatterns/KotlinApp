package com.example.kotlinapp.ui.pokemoninfo

import com.example.kotlinapp.R

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