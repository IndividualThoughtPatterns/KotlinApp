package com.example.kotlinapp

class Pokemon (
    val name: String,
    val sprite: String,
    var types: MutableList<String> = mutableListOf<String>(),
    var abilities: MutableList<String> = mutableListOf<String>(),
    val height: String,
    val weight: String,
    val hp: String,
    val defense: String,
    val attack: String,
    val speed: String,
)