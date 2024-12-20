package com.example.kotlinapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import kotlinx.serialization.Serializable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            navController.graph = navController.createGraph(
                startDestination = PokemonList
            ) {
                fragment<PokemonListFragment, PokemonList> {
                    label = "Pokemon List"
                }
                fragment<PokemonInfoFragment, PokemonInfo> {
                    label = "Pokemon"
                }
            }
        }
    }

    @Serializable
    object PokemonList
}





