package com.example.kotlinapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter(private val names: List<String>) :
    RecyclerView.Adapter<PokemonAdapter.Viewholder>() {

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonNameTV = itemView.findViewById<TextView>(R.id.pokemonNameTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemon_element, parent, false)
        return Viewholder(itemView)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.pokemonNameTV.text = names[position]
    }
}