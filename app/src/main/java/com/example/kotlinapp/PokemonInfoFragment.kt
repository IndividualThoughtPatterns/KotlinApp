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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater)
        val args: Bundle = requireArguments()

        val name = args.getString("name")
        val sprite = args.getString("sprite")
        val height = "height: ${args.getString("height")} decimetres"
        val weight = "weight: ${args.getString("weight")} hectograms"
        val hp = "hp: ${args.getString("hp")}"
        val defense = "defense: ${args.getString("defense")}"
        val attack = "attack: ${args.getString("attack")}"
        val speed = "speed: ${args.getString("speed")}"
        val types = args.getString("types")
        val abilities = args.getString("abilities")

        binding.pokemonInfoNameTextView.setText(name)
        binding.pokemonInfoHeightTextView.setText(height)
        binding.pokemonInfoWeightTextView.setText(weight)
        binding.pokemonInfoHpTextView.setText(hp)
        binding.pokemonInfoDefenseTextView.setText(defense)
        binding.pokemonInfoAttackTextView.setText(attack)
        binding.pokemonInfoSpeedTextView.setText(speed)
        binding.pokemonInfoTypesTextView.setText(types)
        binding.pokemonInfoAbilitiesTextView.setText(abilities)

        Glide.with(binding.avatarImageView)
            .load(sprite)
            .into(binding.avatarImageView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}