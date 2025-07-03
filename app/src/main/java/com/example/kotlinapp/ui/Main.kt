package com.example.kotlinapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinapp.ui.pokemoninfo.PokemonInfoScreen
import com.example.kotlinapp.ui.pokemonlist.PokemonList

@Composable
fun Main() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "PokemonList") {
        composable("PokemonList") {
            PokemonList(navController)
        }

        composable(
            "PokemonInfo" + "/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { stackEntry ->
            PokemonInfoScreen(name = stackEntry.arguments?.getString("name").toString())
        }
    }
}