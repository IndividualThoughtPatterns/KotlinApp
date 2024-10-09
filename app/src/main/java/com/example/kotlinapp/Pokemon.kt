package com.example.kotlinapp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pokemon(
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
) : Serializable {
    class PokemonNames(
        @SerializedName("results") val names: List<Name>
    ) {
        class Name(
            @SerializedName("name") val name: String
        )
    }

    class PokemonDescription(
        @SerializedName("id") val id: Int,
        @SerializedName("height") val height: Int,
        @SerializedName("weight") val weight: Int,
        @SerializedName("abilities") val abilities: List<AbilityObj>,
        @SerializedName("sprites") val sprites: Sprite,
        @SerializedName("types") val types: List<TypeObj>,
        @SerializedName("stats") val stats: List<Stat>,
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

        class Sprite(
            @SerializedName("front_default") val frontDefault: String,
            @SerializedName("other") val other: OfficialArtwork
        ) {
            class OfficialArtwork(
                @SerializedName("official-artwork") val officialArtwork: FrontDefault
            ) {
                class FrontDefault(
                    @SerializedName("front_default") val frontDefault: String
                )
            }
        }
    }

    class PokemonFlavor(
        @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>
    ) {
        class FlavorTextEntry(
            @SerializedName("flavor_text") val flavorText: String
        )
    }
}