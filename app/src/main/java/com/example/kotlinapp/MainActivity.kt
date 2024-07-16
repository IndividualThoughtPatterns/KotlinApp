package com.example.kotlinapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val limit: Int = 20
        val adapter: PokemonAdapter = PokemonAdapter()

        PokemonsNetwork().getPokemons(
            limit,
            fun(pokemons) {
                runOnUiThread {
                    adapter.setPokemons(pokemons)
                    recyclerView.adapter = adapter
                }
            },
            fun() {
                runOnUiThread {
                    Toast.makeText(this, "Ошибка сети", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}



