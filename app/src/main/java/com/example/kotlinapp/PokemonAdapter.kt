package com.example.kotlinapp

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.PokemonElementBinding
import com.bumptech.glide.Glide

class PokemonAdapter(
    val onPokemonClick: (pokemonItem: PokemonItem) -> Unit,
    val onIsFavoriteClick: (pokemonItem: PokemonItem) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.Viewholder>() {

    inner class Viewholder(private val binding: PokemonElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var item: PokemonItem? = null

        fun bind(pokemonItem: PokemonItem) {
            item = pokemonItem
            binding.pokemonNameTextView.text =
                pokemonItem.name.substring(0, 1).uppercase() + pokemonItem.name.substring(
                    1,
                    pokemonItem.name.length
                )

            binding.isFavoriteTextView.setOnClickListener { onIsFavoriteClick(pokemonItem) }

            if (pokemonItem.isFavorite) {
                binding.isFavoriteTextView.setTextColor(Color.rgb(255, 165, 0))
            } else {
                binding.isFavoriteTextView.setTextColor(Color.rgb(0, 0, 0))
            }

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
        return pokemonItems!!.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        Log.d("adapter", "onBind $position")
        holder.bind(pokemonItems!![position])
    }

    private var pokemonItems: MutableList<PokemonItem>? = null
    fun setPokemons(pokemonItemsList: MutableList<PokemonItem>?) {
        Log.d("adapter", "setPokemons")
        pokemonItems = pokemonItemsList
        notifyDataSetChanged()
    }
}