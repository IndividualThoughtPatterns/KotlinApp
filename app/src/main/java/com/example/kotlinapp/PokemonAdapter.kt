package com.example.kotlinapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter(val onPokemonClick: (pokemonName: String) -> Unit) :
    RecyclerView.Adapter<PokemonAdapter.Viewholder>() {

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonName_textView = itemView.findViewById<TextView>(R.id.pokemonName_textView)
        fun bind(pokemon: Pokemon) {
            pokemonName_textView.text = pokemon.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        Log.d("adapter", "onCreate")
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemon_element, parent, false)
        return Viewholder(itemView)
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        Log.d("adapter", "onBind $position")
        holder.bind(pokemons[position])
        holder.itemView.setOnClickListener {
            val name = this.pokemons[position].name

            onPokemonClick(name)
        }
    }

    private var pokemons: List<Pokemon> = emptyList<Pokemon>()
    fun setPokemons(pokemonList: List<Pokemon>) {
        Log.d("adapter", "setPokemons")
        pokemons = pokemonList
        notifyDataSetChanged()
    }
}