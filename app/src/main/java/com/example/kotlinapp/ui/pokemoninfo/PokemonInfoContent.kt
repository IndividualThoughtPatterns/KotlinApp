package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kotlinapp.R
import com.example.kotlinapp.data.BaseStat
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.ui.PokemonLoadingScreen

@Composable
fun PokemonInfoContent(
    state: PokemonInfoScreenState,
    onEvent: (PokemonInfoEvent) -> Unit,
    modifier: Modifier
) {
    when (state.loadingState) {
        LoadingState.STARTED -> PokemonLoadingScreen(modifier = Modifier)
        LoadingState.SUCCESS -> {
            CompositionLocalProvider(
                LocalPokemon provides state.pokemon!!
            ) {
                LoadedContent(modifier = Modifier)
            }
        }

        LoadingState.FAILED -> ErrorContent(
            modifier = Modifier,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LoadedContent(modifier: Modifier) {
    with(LocalPokemon.current) {
        val mainColor = colorResource(getColor(types[0]))

        Box(
            modifier = modifier.then(
                Modifier
                    .fillMaxSize()
                    .background(mainColor)
            )
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.pokeball20),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(205.dp)
                    .padding(top = 8.dp, end = 8.dp),
                contentDescription = "Background pokeball"
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    // verticalAlignment = Alignment.Bottom // не работает
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 20.dp, top = 30.dp)
                ) {
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Text(
                            text = name.replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Bold,
                            fontSize = 33.sp,
                            color = Color.White,
                        )
                    }
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Text(
                            text = "#" + get3digitValue(value = id),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                    }
                }
                AboutCard(
                    modifier = Modifier
                )
            }
            GlideImage(
                model = bigSprite,
                contentDescription = "pokemon avatar",
                modifier = Modifier
                    .size(220.dp)
                    .align(BiasAlignment(verticalBias = -0.75f, horizontalBias = 0f))
            )
        }
    }
}

@Composable
private fun ErrorContent(
    modifier: Modifier,
    onEvent: (PokemonInfoEvent) -> Unit
) {
    Column(
        modifier = modifier.then(
            Modifier
                .padding(10.dp)
                .fillMaxSize()
        )
    ) {
        Text(
            text = "Ошибка сети. Проверьте соединение с интернетом и попробуйте еще раз.",
        )
        Button(
            onClick = { onEvent(PokemonInfoEvent.OnRetryClick) },
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Text(text = "Загрузить")
        }
    }
}

@Composable
fun AboutCard(modifier: Modifier) {
    Card(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 160.dp,
                    bottom = 8.dp
                )
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        AboutCardContent(modifier = Modifier)
    }
}

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
            PokemonBioSection(
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

@Composable
fun PokemonBioSection(modifier: Modifier) {
    Row(
        modifier = modifier.then(
            Modifier.height(IntrinsicSize.Min) // без этого не будет работать
        ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        WeightSection(modifier = Modifier.fillMaxHeight())
        VerticalDivider(modifier = Modifier)
        HeightSection(modifier = Modifier.fillMaxHeight())
        VerticalDivider(modifier = Modifier)
        AbilitiesSection(modifier = Modifier)
    }
}

@Composable
fun HeightSection(modifier: Modifier) {
    Column(
        modifier = modifier.then(
            Modifier.fillMaxHeight()
        ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.straighten),
                modifier = Modifier
                    .size(25.dp)
                    .padding(bottom = 10.dp, end = 10.dp),
                contentDescription = "height image"
            )
            Text(
                text = "${(LocalPokemon.current.height).toFloat() / 10} m",
                modifier = Modifier
                    .padding(bottom = 10.dp),
            )
        }
        Text(
            text = "Height",
            modifier = Modifier,
            color = Color.Gray,
            fontSize = 11.sp,
        )
    }
}

@Composable
fun WeightSection(modifier: Modifier) {
    Column(
        modifier = modifier.then(
            Modifier.fillMaxHeight()
        ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.weight),
                modifier = Modifier
                    .size(25.dp)
                    .padding(bottom = 10.dp, end = 10.dp),
                contentDescription = "weight image"
            )
            Text(
                text = "${(LocalPokemon.current.weight).toFloat() / 10} kg",
                modifier = Modifier
                    .padding(bottom = 10.dp),
            )
        }
        Text(
            text = "Weight",
            modifier = Modifier,
            color = Color.Gray,
            fontSize = 11.sp,
        )
    }
}

@Composable
fun AbilitiesSection(modifier: Modifier) {
    var abilityNames = ""

    with(LocalPokemon.current) {
        for (i in abilities.indices) {
            abilityNames += abilities[i]
                .replaceFirstChar { it.uppercase() }
            if (i != abilities.size - 1) abilityNames += "\n"
        }
    }

    Column(
        modifier = modifier.then(
            Modifier.fillMaxHeight()
        ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = abilityNames,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Abilities",
            modifier = Modifier,
            color = Color.Gray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BaseStatElement(modifier: Modifier, baseStat: BaseStat, color: Color) {
    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = baseStat.baseStatName,
            textAlign = TextAlign.End,
            modifier = Modifier
                .width(40.dp),
            fontWeight = FontWeight.Bold,
            color = color
        )
        VerticalDivider()
        Text(text = baseStat.baseStatStringValue)
        LinearProgressIndicator(
            progress = { baseStat.baseStatValue.toFloat() / 233f },
            modifier = Modifier.widthIn(max = 210.dp),
            color = color,
            trackColor = color.copy(alpha = 0.24f),
            gapSize = 0.dp,
        )
    }
}

@Composable
fun TypeElement(modifier: Modifier, text: String, color: Color) {
    Card(
        modifier = modifier.then(
            Modifier
                .padding(start = 15.dp)
        ),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Text(
            text = text.replaceFirstChar { it.uppercase() },
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}