//package com.example.kotlinapp.ui.pokemoninfo
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
//import androidx.lifecycle.repeatOnLifecycle
//import com.bumptech.glide.Glide
//import com.example.kotlinapp.R
//import com.example.kotlinapp.data.BaseStat
//import com.example.kotlinapp.databinding.FragmentInfoBinding
//import kotlinx.coroutines.launch
//
//class PokemonInfoFragment : Fragment() {
//    private var _binding: FragmentInfoBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentInfoBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val pokemonInfoViewModel = ViewModelProvider(
//            this,
//            PokemonInfoViewModelFactory()
//        )[PokemonInfoViewModel::class.java]
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
//                pokemonInfoViewModel.pokemonStateFlow.collect {
//                    it?.let {
//                        with(it) {
//                            binding.pokemonInfoLoadingProgressBar.visibility = View.GONE
//                            binding.pokemonInfoErrorConstraintLayout.visibility = View.GONE
//                            binding.pokemonInfoScrollView.visibility = View.VISIBLE
//
//                            val mainColorResId = getColor(types[0])
//                            val mainColor = ContextCompat.getColor(requireContext(), mainColorResId)
//
//                            Glide.with(binding.avatarImageView).load(bigSprite)
//                                .into(binding.avatarImageView)
//
//                            val typesRecyclerView = binding.typesRecyclerView
//                            typesRecyclerView.adapter = TypesAdapter(
//                                types = types,
//                                getColor = ::getColor
//                            )
//
//                            var abilityNames = ""
//                            for (i in abilities.indices) {
//                                abilityNames += abilities[i]
//                                    .replaceFirstChar { it.uppercase() }
//                                if (i != abilities.size - 1) abilityNames += "\n"
//                            }
//
//                            with(binding) {
//                                pokemonInfoNameTextView.text =
//                                    name.replaceFirstChar { it.uppercase() }
//                                pokemonIdTextView.text = "#" + get3digitValue(value = id)
//                                pokemonInfoHeightTextView.text = "${(height).toFloat() / 10} m"
//                                pokemonInfoWeightTextView.text = "${(weight).toFloat() / 10} kg"
//                                pokemonInfoAbilitiesTextView.text = abilityNames
//                                pokemonFlavor.text = flavor
//
//                                val drawable =
//                                    ContextCompat.getDrawable(requireContext(), mainColorResId)
//                                fragmentContainer.background = drawable
//
//                                aboutLabelTextview.setTextColor(mainColor)
//                                baseStatsLabelTextview.setTextColor(mainColor)
//                            }
//
//                            val baseStats = listOf(
//                                BaseStat(
//                                    baseStatName = "HP",
//                                    baseStatStringValue = get3digitValue(value = hp),
//                                    baseStatValue = hp
//                                ),
//                                BaseStat(
//                                    baseStatName = "ATK",
//                                    baseStatStringValue = get3digitValue(value = attack),
//                                    baseStatValue = attack
//                                ),
//                                BaseStat(
//                                    baseStatName = "DEF",
//                                    baseStatStringValue = get3digitValue(value = defense),
//                                    baseStatValue = defense
//                                ),
//                                BaseStat(
//                                    baseStatName = "SPD",
//                                    baseStatStringValue = get3digitValue(value = speed),
//                                    baseStatValue = speed
//                                ),
//                            )
//
//                            val baseStatRecyclerView = binding.baseStatsRecyclerView
//                            baseStatRecyclerView.adapter = BaseStatAdapter(
//                                baseStatList = baseStats,
//                                color = mainColor
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
//                pokemonInfoViewModel.pokemonLoadingState.collect {
//                    it?.let {
//                        if (!it.isLoaded) {
//                            binding.pokemonInfoLoadingConstraintLayout.visibility = View.GONE
//                            binding.pokemonInfoErrorConstraintLayout.visibility = View.VISIBLE
//                            binding.pokemonInfoErrorTryAgainButton.setOnClickListener {
//                                binding.pokemonInfoLoadingConstraintLayout.visibility = View.VISIBLE
//                                pokemonInfoViewModel.loadPokemon()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun get3digitValue(value: Int): String {
//        return when (value) {
//            in 0..9 -> "00${value}"
//            in 10..99 -> "0${value}"
//            else -> "$value"
//        }
//    }
//
//    private fun getColor(type: String): Int {
//        return when (type) {
//            "normal" -> R.color.normal
//            "fire" -> R.color.fire
//            "water" -> R.color.water
//            "grass" -> R.color.grass
//            "electric" -> R.color.electric
//            "ice" -> R.color.ice
//            "fighting" -> R.color.fighting
//            "poison" -> R.color.poison
//            "ground" -> R.color.ground
//            "flying" -> R.color.flying
//            "psychic" -> R.color.psychic
//            "bug" -> R.color.bug
//            "rock" -> R.color.rock
//            "ghost" -> R.color.ghost
//            "dark" -> R.color.dark
//            "dragon" -> R.color.dragon
//            "steel" -> R.color.steel
//            "fairy" -> R.color.fairy
//            "stellar" -> R.color.stellar
//            "unknown" -> R.color.unknown
//            else -> R.color.unknown
//        }
//    }
//}