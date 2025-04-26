package com.example.kotlinapp.ui.pokemonlist

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.databinding.PokemonElementBinding

class PokemonAdapter(
    val onPokemonClick: (pokemonItem: PokemonItem) -> Unit,
    val onIsFavoriteClick: (pokemonItem: PokemonItem) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PokemonElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var item: PokemonItem? = null

        fun bind(pokemonItem: PokemonItem) {
            item = pokemonItem
            binding.pokemonNameTextView.text = pokemonItem.name.replaceFirstChar { it.uppercase() }

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
            binding.isFavoriteTextView.setOnClickListener {
                item?.let(onIsFavoriteClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("adapter", "onCreate")

        val inflater = LayoutInflater.from(parent.context)
        val binding = PokemonElementBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pokemonItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("adapter", "onBind $position")
        holder.bind(pokemonItems[position])
    }

    private var pokemonItems = emptyList<PokemonItem>()
    fun setPokemons(pokemonItemsList: List<PokemonItem>) {
        Log.d("adapter", "setPokemons")
        pokemonItems = pokemonItemsList
        notifyDataSetChanged()
    }
}