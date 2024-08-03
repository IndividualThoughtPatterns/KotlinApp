package com.example.kotlinapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.PokemonElementBinding
import com.bumptech.glide.Glide

class PokemonAdapter(val onPokemonClick: (pokemon: Pokemon) -> Unit) :
    RecyclerView.Adapter<PokemonAdapter.Viewholder>() {

    inner class Viewholder(val binding: PokemonElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var item: Pokemon? = null
        val pokemonName_textView = binding.pokemonNameTextView
        val avatar_imageVies = binding.avatarImageView

        fun bind(pokemon: Pokemon) {
            item = pokemon
            pokemonName_textView.text = pokemon.name
            Glide.with(avatar_imageVies)
                .load(item?.sprite)
                .into(avatar_imageVies)
        }

        init {
            itemView.setOnClickListener {
                item?.let {
                    onPokemonClick(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        Log.d("adapter", "onCreate")

        val inflater = LayoutInflater.from(parent.context)
        val binding = PokemonElementBinding.inflate(inflater, parent, false)
        return Viewholder(binding)
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        Log.d("adapter", "onBind $position")
        holder.bind(pokemons[position])
    }

    private var pokemons: List<Pokemon> = emptyList<Pokemon>()
    fun setPokemons(pokemonList: List<Pokemon>) {
        Log.d("adapter", "setPokemons")
        pokemons = pokemonList
        notifyDataSetChanged()
    }
}