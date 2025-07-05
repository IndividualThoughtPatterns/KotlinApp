package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.data.BaseStat

@Composable
fun AboutCardContent(modifier: Modifier) {
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
        Column(
            modifier = modifier.then(
                Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
            ),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(top = 50.dp)
            ) {
                itemsIndexed(types) { index, type ->
                    TypeElement(
                        modifier = Modifier,
                        text = type,
                        color = colorResource(
                            getColor(types[index])
                        )
                    )
                }
            }
            Text(
                text = "About",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = mainColor,
                textAlign = TextAlign.Center
            )
            PokemonBioConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = flavor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .verticalScroll(ScrollState(0)),
                fontSize = 12.sp
            )
            Text(
                text = "Base Stats",
                modifier = Modifier
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                color = mainColor
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(baseStats) { baseStat ->
                    BaseStatElement(
                        modifier = Modifier,
                        baseStat = baseStat,
                        color = mainColor
                    )
                }
            }
        }
    }
}