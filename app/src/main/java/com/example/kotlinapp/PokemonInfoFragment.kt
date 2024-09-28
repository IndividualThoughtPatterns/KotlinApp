package com.example.kotlinapp

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.generateViewId
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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

        val typesLayout = binding.typesLayout
        val typeCardViewList = mutableListOf<CardView>()

        for (i in 0 until pokemon.types.size) {
            typeCardViewList.add(CardView(requireContext()))
            typeCardViewList[i].id = generateViewId()
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT , ConstraintLayout.LayoutParams.WRAP_CONTENT)

            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID

            typeCardViewList[i].layoutParams = layoutParams
            typeCardViewList[i].radius = 50F
            typeCardViewList[i].setCardBackgroundColor(resources.getColor(getColor(pokemon.types[i])))

            typesLayout.addView(typeCardViewList[i])

            val typeTextview = TextView(requireContext())
            val typeTextViewLP = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )

            typeTextViewLP.setMargins(20, 10, 20, 10)
            typeTextview.layoutParams = typeTextViewLP
            typeTextview.gravity = Gravity.CENTER
            typeTextview.text = pokemon.types[i].replaceFirstChar { it.uppercase() }
            typeTextview.setTypeface(typeTextview.typeface, Typeface.BOLD)
            typeTextview.setTextColor(resources.getColor(R.color.white))
            typeCardViewList[i].addView(typeTextview)
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(typesLayout)

        constraintSet.createHorizontalChain(
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            typeCardViewList.map { it.id}.toIntArray(),
            null,
            ConstraintSet.CHAIN_PACKED
        )

        var previousItem: View? = null

        for (cardView in typeCardViewList) {
            val lastItem = typeCardViewList.indexOf(cardView) == typeCardViewList.size - 1
            if (previousItem == null) {
                constraintSet.connect(
                    cardView.id,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT
                )
            } else {
                constraintSet.connect(
                    cardView.id,
                    ConstraintSet.LEFT,
                    previousItem.id,
                    ConstraintSet.RIGHT,
                    20
                )
                if (lastItem) {
                    constraintSet.connect(
                        cardView.id,
                        ConstraintSet.RIGHT,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.RIGHT
                    )
                }
            }
            previousItem = cardView
        }

        constraintSet.applyTo(typesLayout)

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
        binding.baseStatsHPValueTextView.text = get3digitValue(value = pokemon.hp)
        binding.baseStatsAttackValueTextView.text = get3digitValue(value = pokemon.attack)
        binding.baseStatsDefenseValueTextView.text = get3digitValue(value = pokemon.defense)
        binding.baseStatsSpeedValueTextView.text = get3digitValue(value = pokemon.speed)

        binding.pokemonFlavor.text = pokemon.flavor

        val drawable = resources.getDrawable(mainColor)
        binding.fragmentContainer.background = drawable

        mainColor = resources.getColor(mainColor)

        binding.aboutLabelTextview.setTextColor(mainColor)
        binding.baseStatsLabelTextview.setTextColor(mainColor)
        binding.baseStatsHPlabelTextView.setTextColor(mainColor)
        binding.baseStatsAttacklabelTextView.setTextColor(mainColor)
        binding.baseStatsDefenselabelTextView.setTextColor(mainColor)
        binding.baseStatsSpeedlabelTextView.setTextColor(mainColor)

        Glide.with(binding.avatarImageView).load(pokemon.bigSprite).into(binding.avatarImageView)

        fillProgressBar(progressBar = binding.hpProgressBar, currentValue = pokemon.hp, color = mainColor)
        fillProgressBar(progressBar = binding.attackProgressBar, currentValue = pokemon.attack, color = mainColor)
        fillProgressBar(progressBar = binding.defenseProgressBar, currentValue = pokemon.defense, color = mainColor)
        fillProgressBar(progressBar = binding.speedProgressBar, currentValue = pokemon.speed, color = mainColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun get3digitValue (value: Int): String {
        return when (value) {
            in 0..9 -> "00${value}"
            in 10..99 -> "0${value}"
            else -> "${value}"
        }
    }

    private fun fillProgressBar(
        progressBar: ProgressBar,
        currentValue: Int,
        color: Int
    ) {
        progressBar.progressTintList = ColorStateList.valueOf(color)
        progressBar.progressBackgroundTintList = ColorStateList.valueOf(color)
        progressBar.max = 233
        progressBar.progress = currentValue
    }

    private fun getColor (type: String): Int {
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