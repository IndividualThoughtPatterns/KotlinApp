package com.example.kotlinapp.pokemon_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kotlinapp.App
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
        App.pokemonInfoViewModel = ViewModelProvider(
            requireActivity(),
            PokemonInfoViewModelFactory(
                fragment = this,
                app = requireActivity().application
            )
        )[PokemonInfoViewModel::class.java]

        var mainColor = App.pokemonInfoViewModel.mainColor

        Glide.with(binding.avatarImageView).load(
            App.pokemonInfoViewModel.pokemon.bigSprite
        ).into(binding.avatarImageView)

        val typesRecyclerView = binding.typesRecyclerView
        typesRecyclerView.adapter = App.pokemonInfoViewModel.typesAdapter

        with(binding) {
            pokemonInfoNameTextView.text = App.pokemonInfoViewModel.pokemon.name
                .replaceFirstChar { it.uppercase() }
            pokemonIdTextView.text = "#" + App.pokemonInfoViewModel.pokemonId3digitValue
            pokemonInfoHeightTextView.text =
                "${(App.pokemonInfoViewModel.pokemon.height).toFloat() / 10} m"
            pokemonInfoWeightTextView.text =
                "${(App.pokemonInfoViewModel.pokemon.weight).toFloat() / 10} kg"
            pokemonInfoAbilitiesTextView.text = App.pokemonInfoViewModel.getAbilityNames()
            pokemonFlavor.text = App.pokemonInfoViewModel.pokemon.flavor

            val drawable = ContextCompat.getDrawable(requireContext(), mainColor)
            fragmentContainer.background = drawable

            mainColor = ContextCompat.getColor(requireContext(), mainColor)

            aboutLabelTextview.setTextColor(mainColor)
            baseStatsLabelTextview.setTextColor(mainColor)
        }

        val baseStatRecyclerView = binding.baseStatsRecyclerView
        baseStatRecyclerView.adapter = App.pokemonInfoViewModel.baseStatAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}