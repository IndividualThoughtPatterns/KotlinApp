package com.example.kotlinapp

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.PokemonElementBinding
import com.bumptech.glide.Glide

class PokemonAdapter(
    val onPokemonClick: (pokemon: Pokemon) -> Unit,
    val onIsFavoriteClick: (pokemon: Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.Viewholder>() {

    inner class Viewholder(val binding: PokemonElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var item: Pokemon? = null

        fun bind(pokemon: Pokemon) {
            item = pokemon
            binding.pokemonNameTextView.text =
                pokemon.name.substring(0, 1).uppercase() + pokemon.name.substring(
                    1,
                    pokemon.name.length
                )

            if (pokemon.isFavorite) {
                binding.isFavoriteTextView.setTextColor(Color.rgb(255, 165, 0))
            }

            binding.isFavoriteTextView.setOnClickListener { onIsFavoriteClick(pokemon) }
            Glide.with(binding.avatarImageView).load(item?.sprite).into(binding.avatarImageView)
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

    private var pokemons: List<Pokemon> = emptyList()
    fun setPokemons(pokemonList: List<Pokemon>) {
        Log.d("adapter", "setPokemons")
        pokemons = pokemonList
        notifyDataSetChanged()
    }
}