package com.example.kotlinapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.kotlinapp.databinding.FragmentInfoBinding

class PokemonInfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: Bundle = requireArguments()
        val pokemon = args.getSerializable("pokemon") as Pokemon

        var typeNames = "| "
        for (i in 0 until pokemon.types.size) {
            typeNames += pokemon.types[i] + " | "
        }
        var abilityNames = "Abilities: "
        for (i in 0 until pokemon.abilities.size) {
            abilityNames += pokemon.abilities[i]
            if (i != pokemon.abilities.size - 1) abilityNames += ", "
        }

        binding.pokemonInfoNameTextView.text =
            "${pokemon.name.substring(0, 1).uppercase()}" +
                    "${pokemon.name.substring(1, pokemon.name.length)}"

        binding.pokemonInfoHeightTextView.text = "Height: ${pokemon.height} decimetres"
        binding.pokemonInfoWeightTextView.text = "Weight: ${pokemon.weight} hectograms"
        binding.pokemonInfoHpTextView.text = "HP: ${pokemon.hp}"
        binding.pokemonInfoDefenseTextView.text = "Defense: ${pokemon.defense}"
        binding.pokemonInfoAttackTextView.text = "Attack: ${pokemon.attack}"
        binding.pokemonInfoSpeedTextView.text = "Speed: ${pokemon.speed}"
        binding.pokemonInfoTypesTextView.text = typeNames
        binding.pokemonInfoAbilitiesTextView.text = abilityNames

        Glide.with(binding.avatarImageView).load(pokemon.sprite).into(binding.avatarImageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}