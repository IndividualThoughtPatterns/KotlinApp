package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.kotlinapp.data.BaseStat

@Composable
fun AboutCardContent() {
    with(LocalPokemon.current) {
        val mainColor = colorResource(getColor(types[0]))
        val baseStats = listOf(
            BaseStat(
                baseStatName = "HP",
                baseStatStringValue = get3digitValue(value = hp),
                baseStatValue = hp
            ),
            BaseStat(
                baseStatName = "ATK",
                baseStatStringValue = get3digitValue(value = attack),
                baseStatValue = attack
            ),
            BaseStat(
                baseStatName = "DEF",
                baseStatStringValue = get3digitValue(value = defense),
                baseStatValue = defense
            ),
            BaseStat(
                baseStatName = "SPD",
                baseStatStringValue = get3digitValue(value = speed),
                baseStatValue = speed
            ),
        )
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            val (
                typesLazyRowRef,
                aboutLabelTextRef,
                pokemonBioConstraintLayoutRef,
                pokemonFlavorScrollRef,
                baseStatsLabelTextRef,
                baseStatsLazyColumnRef
            ) = createRefs()

            LazyRow(
                modifier = Modifier
                    .constrainAs(typesLazyRowRef) {
                        bottom.linkTo(aboutLabelTextRef.top)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        top.linkTo(parent.top)
                    }
                    .padding(top = 50.dp)
            ) {
                itemsIndexed(types) { index, type ->
                    TypeElement(type, colorResource(getColor(types[index])))
                }
            }
            Text(
                text = "About",
                modifier = Modifier
                    .constrainAs(aboutLabelTextRef) {
                        bottom.linkTo(pokemonBioConstraintLayoutRef.top)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        top.linkTo(typesLazyRowRef.bottom)
                    },
                fontSize = 17.sp,
                color = mainColor
            )
            PokemonBioConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(pokemonBioConstraintLayoutRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        bottom.linkTo(pokemonFlavorScrollRef.top)
                        top.linkTo(aboutLabelTextRef.bottom)
                    }
            )
            Text(
                text = flavor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .constrainAs(pokemonFlavorScrollRef) {
                        bottom.linkTo(baseStatsLabelTextRef.top)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        top.linkTo(pokemonBioConstraintLayoutRef.bottom)
                    }
                    //.verticalScroll(ScrollState(0))
                    .padding(top = 30.dp),
                fontSize = 12.sp
            )
            Text(
                text = "Base Stats",
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(baseStatsLabelTextRef) {
                        bottom.linkTo(baseStatsLazyColumnRef.top)
                        top.linkTo(pokemonFlavorScrollRef.bottom)
                    }
                    .padding(top = 20.dp, bottom = 15.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                color = mainColor
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(baseStatsLazyColumnRef) {
                        bottom.linkTo(parent.bottom)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        top.linkTo(baseStatsLabelTextRef.bottom)
                    }
            ) {
                items(baseStats) { baseStat ->
                    BaseStatElement(baseStat = baseStat, color = mainColor)
                }
            }
        }
    }
}