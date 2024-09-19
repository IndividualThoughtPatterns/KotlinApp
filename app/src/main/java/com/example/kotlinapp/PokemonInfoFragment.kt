package com.example.kotlinapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
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

        for (i in 0 until pokemon.types.size) {
            val typeCardView = CardView(requireContext())
            val layoutParams = FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                Gravity.END
            )
            layoutParams.setMargins(0, 0, 30, 0)
            typeCardView.layoutParams = layoutParams
            typeCardView.radius = 50F
            typeCardView.setCardBackgroundColor(getResources().getColor(getColor(pokemon.types[i])))

            typesLayout.addView(typeCardView)

            val typeTextview = TextView(requireContext())
            val typeTextViewLP = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            typeTextViewLP.setMargins(20, 10, 20, 10)
            typeTextview.layoutParams = typeTextViewLP
            typeTextview.gravity = Gravity.CENTER
            typeTextview.text = pokemon.types[i].replaceFirstChar { it.uppercase() }
            typeTextview.setTypeface(typeTextview.getTypeface(), Typeface.BOLD)
            typeTextview.setTextColor(getResources().getColor(R.color.white))
            typeCardView.addView(typeTextview)
        }

        var abilityNames = ""
        for (i in 0 until pokemon.abilities.size) {
            abilityNames += pokemon.abilities[i].replaceFirstChar { it.uppercase() }
            if (i != pokemon.abilities.size - 1) abilityNames += "\n"
        }

        binding.pokemonInfoNameTextView.text = pokemon.name.replaceFirstChar { it.uppercase() }

        binding.pokemonInfoHeightTextView.text = "${(pokemon.height).toFloat() / 10} m"
        binding.pokemonInfoWeightTextView.text = "${(pokemon.weight).toFloat() / 10} kg"
        binding.pokemonInfoHpTextView.text = get3digitValue(pokemon.hp)
        binding.pokemonInfoDefenseTextView.text = get3digitValue(pokemon.defense)
        binding.pokemonInfoAttackTextView.text = get3digitValue(pokemon.attack)
        binding.pokemonInfoSpeedTextView.text = get3digitValue(pokemon.speed)
        binding.pokemonInfoAbilitiesTextView.text = abilityNames
        binding.pokemonIdTextView.text = "#" + get3digitValue(pokemon.id)
        binding.pokemonFlavor.text = pokemon.flavor

        val drawable = resources.getDrawable(mainColor)
        binding.fragmentContainer.background = drawable

        mainColor = resources.getColor(mainColor)

        binding.aboutLabelTextview.setTextColor(mainColor)
        binding.baseStatsLabelTextview.setTextColor(mainColor)
        binding.pokemonInfoHpLabel.setTextColor(mainColor)
        binding.pokemonInfoAttackLabel.setTextColor(mainColor)
        binding.pokemonInfoDefenseLabel.setTextColor(mainColor)
        binding.pokemonInfoSpeedLabel.setTextColor(mainColor)

        Glide.with(binding.avatarImageView).load(pokemon.bigSprite).into(binding.avatarImageView)

        val maxStatValue = 233F
        val maxLineLength = 400F
        val statFactor = maxLineLength / maxStatValue

        binding.baseStatsLayout.addView(
            DrawView(requireContext(),
                color = mainColor,
                hpValue = pokemon.hp * statFactor,
                attackValue = pokemon.attack * statFactor,
                defenseValue = pokemon.defense * statFactor,
                speedValue = pokemon.speed * statFactor)
        )
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

class DrawView(
    context: Context,
    private val color: Int,
    private val hpValue: Float,
    private val attackValue: Float,
    private val defenseValue: Float,
    private val speedValue: Float
) : View(context) {
    private val printBright = Paint()
    private val printTransparent = Paint()
    private val colorWithAlpha = ColorUtils.setAlphaComponent(color, 64)
    private val lineSpace = 48F

    override fun onDraw(canvas: Canvas) {
        printBright.setColor(color)
        printBright.strokeWidth = 10F

        printTransparent.setColor(colorWithAlpha)
        printTransparent.strokeWidth = 10F

        canvas.drawLine(0F,21F,hpValue,21F, printBright)
        canvas.drawLine(hpValue,21F,400F,21F, printTransparent)

        canvas.drawLine(0F,21F + lineSpace, attackValue,21F + lineSpace, printBright)
        canvas.drawLine(attackValue,21F + lineSpace,400F,21F + lineSpace, printTransparent)

        canvas.drawLine(0F,21F + lineSpace * 2, defenseValue,21F + lineSpace * 2, printBright)
        canvas.drawLine(defenseValue,21F + lineSpace * 2,400F,21F + lineSpace * 2, printTransparent)

        canvas.drawLine(0F,21F + lineSpace * 3, speedValue,21F + lineSpace * 3, printBright)
        canvas.drawLine(speedValue,21F + lineSpace * 3,400F,21F + lineSpace * 3, printTransparent)
    }
}