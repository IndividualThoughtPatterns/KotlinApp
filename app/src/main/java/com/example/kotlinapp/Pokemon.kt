package com.example.kotlinapp

import java.io.Serializable

class Pokemon(
    val name: String,
    val sprite: String,
    var types: MutableList<String>,
    var abilities: List<String>,
    val height: String,
    val weight: String,
    val hp: String,
    val defense: String,
    val attack: String,
    val speed: String,
) : Serializable