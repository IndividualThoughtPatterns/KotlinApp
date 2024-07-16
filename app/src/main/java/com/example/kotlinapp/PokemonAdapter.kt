package com.example.kotlinapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter() :
    RecyclerView.Adapter<PokemonAdapter.Viewholder>() {

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonName_textView = itemView.findViewById<TextView>(R.id.pokemonName_textView)
        fun bind(pokemon: Pokemon) {
            pokemonName_textView.text = pokemon.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemon_element, parent, false)
        return Viewholder(itemView)
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(pokemons[position])
    }

    private lateinit var pokemons: List<Pokemon>

    fun setPokemons(pokemonList: List<Pokemon>) {
        pokemons = pokemonList
    }
}