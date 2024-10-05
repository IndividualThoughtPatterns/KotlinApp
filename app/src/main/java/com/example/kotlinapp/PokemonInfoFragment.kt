package com.example.kotlinapp

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

        var mainColor = getColor(pokemon.types[0])

        Glide.with(binding.avatarImageView).load(pokemon.bigSprite).into(binding.avatarImageView)

        val typesRecyclerView = binding.typesRecyclerView
        typesRecyclerView.adapter = TypesAdapter(types = pokemon.types, getColor = ::getColor)

        var abilityNames = ""
        for (i in 0 until pokemon.abilities.size) {
            abilityNames += pokemon.abilities[i].replaceFirstChar { it.uppercase() }
            if (i != pokemon.abilities.size - 1) abilityNames += "\n"
        }

        binding.pokemonInfoNameTextView.text = pokemon.name.replaceFirstChar { it.uppercase() }
        binding.pokemonIdTextView.text = "#" + get3digitValue(value = pokemon.id)
        binding.pokemonInfoHeightTextView.text = "${(pokemon.height).toFloat() / 10} m"
        binding.pokemonInfoWeightTextView.text = "${(pokemon.weight).toFloat() / 10} kg"
        binding.pokemonInfoAbilitiesTextView.text = abilityNames
        binding.pokemonFlavor.text = pokemon.flavor

        val drawable = ContextCompat.getDrawable(requireContext(), mainColor)
        binding.fragmentContainer.background = drawable

        mainColor = ContextCompat.getColor(requireContext(), mainColor)

        val baseStats = listOf(
            BaseStat("HP", get3digitValue(value = pokemon.hp), pokemon.hp),
            BaseStat("ATK", get3digitValue(value = pokemon.attack), pokemon.attack),
            BaseStat("DEF", get3digitValue(value = pokemon.defense), pokemon.defense),
            BaseStat("SPD", get3digitValue(value = pokemon.speed), pokemon.speed)
        )

        val baseStatRecyclerView = binding.baseStatsRecyclerView
        baseStatRecyclerView.adapter = BaseStatAdapter(
            baseStatList = baseStats,
            fillProgressBar = ::fillProgressBar,
            color = mainColor
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun get3digitValue(value: Int): String {
        return when (value) {
            in 0..9 -> "00${value}"
            in 10..99 -> "0${value}"
            else -> "${value}"
        }
    }

    private fun fillProgressBar(
        progressBar: ProgressBar, currentValue: Int, color: Int
    ) {
        progressBar.progressTintList = ColorStateList.valueOf(color)
        progressBar.progressBackgroundTintList = ColorStateList.valueOf(color)
        progressBar.max = 233
        progressBar.progress = currentValue
    }

    private fun getColor(type: String): Int {
        return when (type) {
            "normal" -> R.color.normal
            "fire" -> R.color.fire
            "water" -> R.color.water
            "grass" -> R.color.grass
            "electric" -> R.color.electric
            "ice" -> R.color.ice
            "fighting" -> R.color.fighting
            "poison" -> R.color.poison
            "ground" -> R.color.ground
            "flying" -> R.color.flying
            "psychic" -> R.color.psychic
            "bug" -> R.color.bug
            "rock" -> R.color.rock
            "ghost" -> R.color.ghost
            "dark" -> R.color.dark
            "dragon" -> R.color.dragon
            "steel" -> R.color.steel
            "fairy" -> R.color.fairy
            "stellar" -> R.color.stellar
            "unknown" -> R.color.unknown
            else -> R.color.unknown
        }
    }
}