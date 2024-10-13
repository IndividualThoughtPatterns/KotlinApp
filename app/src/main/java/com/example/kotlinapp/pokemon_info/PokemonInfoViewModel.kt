package com.example.kotlinapp.pokemon_info

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.kotlinapp.Pokemon
import com.example.kotlinapp.R

class PokemonInfoViewModel(fragment: PokemonInfoFragment) : ViewModel() {

    private val args: Bundle = fragment.requireArguments()
    val pokemon = args.getSerializable("pokemon") as Pokemon

    val mainColor = getColor(pokemon.types[0])

    val typesAdapter = TypesAdapter(
        types = pokemon.types,
        getColor = ::getColor
    )

    private val baseStats = listOf(
        BaseStat("HP", get3digitValue(value = pokemon.hp), pokemon.hp),
        BaseStat("ATK", get3digitValue(value = pokemon.attack), pokemon.attack),
        BaseStat("DEF", get3digitValue(value = pokemon.defense), pokemon.defense),
        BaseStat("SPD", get3digitValue(value = pokemon.speed), pokemon.speed)
    )

    val baseStatAdapter = BaseStatAdapter(
        baseStatList = baseStats,
        color = ContextCompat.getColor(fragment.requireContext(), mainColor)
    )

    val pokemonId3digitValue =
        get3digitValue(value = pokemon.id)

    fun getAbilityNames(): String {
        var abilityNames = ""
        for (i in 0 until pokemon.abilities.size) {
            abilityNames += pokemon.abilities[i]
                .replaceFirstChar { it.uppercase() }
            if (i != pokemon.abilities.size - 1) abilityNames += "\n"
        }
        return abilityNames
    }

    private fun get3digitValue(value: Int): String {
        return when (value) {
            in 0..9 -> "00${value}"
            in 10..99 -> "0${value}"
            else -> "$value"
        }
    }

    private fun getColor(type: String): Int {
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
}