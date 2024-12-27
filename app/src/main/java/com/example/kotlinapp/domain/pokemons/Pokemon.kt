package com.example.kotlinapp.domain.pokemons

data class Pokemon(
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