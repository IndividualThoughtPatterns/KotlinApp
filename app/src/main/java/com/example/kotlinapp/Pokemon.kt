package com.example.kotlinapp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pokemon(
    val name: String,
    val sprite: String,
    val types: MutableList<String>,
    val abilities: List<String>,
    val height: String,
    val weight: String,
    val hp: String,
    val defense: String,
    val attack: String,
    val speed: String
) : Serializable {
    class PokemonNames(
        @SerializedName("results") val names: List<Name>
    ) {
        class Name(
            @SerializedName("name") val name: String
        )
    }

    class PokemonDescription (
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