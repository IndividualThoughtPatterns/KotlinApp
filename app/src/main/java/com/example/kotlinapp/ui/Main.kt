package com.example.kotlinapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.kotlinapp.ui.pokemoninfo.PokemonInfoScreen
import com.example.kotlinapp.ui.pokemonlist.PokemonList
import kotlinx.serialization.Serializable

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("LocalNavController not present")
}

@Composable
fun Main() {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalNavController provides navController,
    ) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.PokemonList
        ) {
            composable<NavRoutes.PokemonList> {
                PokemonList()
            }

            composable<NavRoutes.PokemonInfo> {
                val route: NavRoutes.PokemonInfo = it.toRoute()
                PokemonInfoScreen(route.name)
            }
        }
    }
}

sealed interface NavRoutes {
    @Serializable
    data object PokemonList : NavRoutes

    @Serializable
    data class PokemonInfo(val name: String) : NavRoutes
}