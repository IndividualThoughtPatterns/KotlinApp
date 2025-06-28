package com.example.kotlinapp.ui

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kotlinapp.R
import com.example.kotlinapp.data.BaseStat
import com.example.kotlinapp.data.Pokemon
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.ui.pokemoninfo.PokemonInfoViewModel
import com.example.kotlinapp.ui.pokemonlist.PokemonListViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pokemonListViewModel = ViewModelProvider(this)[PokemonListViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                pokemonListViewModel.pokemonItemListFlow.collect {
                    setContent {
                        Main(it, pokemonListViewModel)
                    }
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                pokemonListViewModel.nextPageLoadingStateFlow.collect {
                    it?.let {
                        if (it.isLoaded) {
                            showSuccessMessage()
                        } else {
                            handleNetworkError()
                            Log.d(
                                "next page loading failure",
                                it.error!!.message.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleNetworkError() {
        Toast.makeText(this, "Ошибка сети", Toast.LENGTH_LONG).show()
    }

    private fun showSuccessMessage() {
        Toast.makeText(this, "Загрузка завершена", Toast.LENGTH_LONG).show()
    }


}

private fun get3digitValue(value: Int): String {
    return when (value) {
        in 0..9 -> "00${value}"
        in 10..99 -> "0${value}"
        else -> "$value"
    }
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Main(
    pokemonItemsList: List<PokemonItem>,
    pokemonListViewModel: PokemonListViewModel,
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "PokemonList") {
        composable("PokemonList") {
            PokemonList(pokemonItemsList, pokemonListViewModel, navController)
        }

        composable(
            "PokemonInfoLoading" + "/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { stackEntry ->
            val name = stackEntry.arguments?.getString("name")
            PokemonInfoLoadingConstraintLayout(
                navController = navController,
                name = name.toString()
            )
        }

        composable(
            "PokemonInfo" + "/{pokemonJson}",
            arguments = listOf(navArgument("pokemonJson") { type = NavType.StringType })
        ) { stackEntry ->
            val pokemonJson = stackEntry.arguments?.getString("pokemonJson")

            val decodedPokemonJson = pokemonJson?.let { Uri.decode(it) }

            val gson = Gson()
            val pokemon: Pokemon? = gson.fromJson(decodedPokemonJson, Pokemon::class.java)


            if (pokemon != null) {
                PokemonInfo(pokemon)
            }
        }
    }
}

@Composable
fun PokemonList(
    pokemonItemsList: List<PokemonItem>,
    pokemonListViewModel: PokemonListViewModel,
    navController: NavController // для проброски вроде был какой-то local че-то там вроде
) {
    val state = rememberLazyListState()
    LaunchedEffect(state) {
        snapshotFlow {
            !state.canScrollForward && (state.layoutInfo.totalItemsCount != 0)
        }.collect {
            if (!state.canScrollForward) {
                pokemonListViewModel.loadNextPage()
            }
        }
    }
    LazyColumn(state = state) {
        items(pokemonItemsList) { pokemonItem ->
            PokemonElement(pokemonItem = pokemonItem, navController)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonElement(pokemonItem: PokemonItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("PokemonInfoLoading/${pokemonItem.name}")
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = pokemonItem.sprite,
                contentDescription = "sprite url",
                modifier = Modifier
                    .size(54.dp)
                    .padding(8.dp)
            )
            Text(
                pokemonItem.name.replaceFirstChar { it.uppercase() },
                modifier = Modifier
                    .weight(6f),
                style = MaterialTheme.typography.headlineSmall,
            )
            IconButton(
                onClick = {},
                modifier = Modifier
                    .weight(2f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.StarOutline,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun TypeElement(text: String, color: Color) {
    Card(
        modifier = Modifier
            //.background(color)
            .padding(start = 15.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = color // Устанавливаем цвет фона карточки
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

@Composable
fun BaseStatElement(baseStat: BaseStat, color: Color) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        val (baseStatsLabelTextRef,
            baseStatsLabelsValuesDividerRef,
            baseStatsValueTextRef,
            hpProgressBarRef
        ) = createRefs()

        Text(
            text = baseStat.baseStatName,
            modifier = Modifier
                .constrainAs(baseStatsLabelTextRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                }
                .width(40.dp),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold,
            color = color
        )
        VerticalDivider(
            modifier = Modifier
                .constrainAs(baseStatsLabelsValuesDividerRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(baseStatsLabelTextRef.absoluteRight)
                }
                .padding(start = 15.dp)
        )
        Text(
            text = baseStat.baseStatStringValue,
            modifier = Modifier
                .width(40.dp)
                .constrainAs(baseStatsValueTextRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(baseStatsLabelsValuesDividerRef.absoluteRight)
                }
                .padding(start = 15.dp)
        )
        LinearProgressIndicator(
            progress = { baseStat.baseStatValue.toFloat() / 233f },
            modifier = Modifier
                .constrainAs(hpProgressBarRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(baseStatsValueTextRef.absoluteRight)
                    absoluteRight.linkTo(parent.absoluteRight)
                }
                .padding(start = 15.dp),
            color = color,
            trackColor = color.copy(alpha = 0.24f),
            gapSize = 0.dp,
        )
    }
}


///// ==========  PokemonInfoScreen ======================================== ////////

@Composable
fun PokemonBioConstraintLayout(modifier: Modifier, pokemon: Pokemon) {
    ConstraintLayout(
        modifier = modifier
            .then(Modifier.height(IntrinsicSize.Min)) // без этого пиздец будет
    ) {
        val (
            weightConstraintLayoutRef,
            weightHeightDividerRef,
            heightConstraintLayoutRef,
            heightAbilitiesDividerRef,
            abilitiesConstraintLayout,
        ) = createRefs()

        WeightConstraintLayout(
            modifier = Modifier
                .constrainAs(weightConstraintLayoutRef) {
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(weightHeightDividerRef.absoluteLeft)
                    top.linkTo(parent.top)
                }
                .fillMaxHeight(), pokemon
        )
        VerticalDivider(
            modifier = Modifier.constrainAs(weightHeightDividerRef) {
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(weightConstraintLayoutRef.absoluteRight)
                absoluteRight.linkTo(heightConstraintLayoutRef.absoluteLeft)
                top.linkTo(parent.top)
            }
        )
        HeightConstraintLayout(
            modifier = Modifier
                .constrainAs(heightConstraintLayoutRef) {
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(weightHeightDividerRef.absoluteRight)
                    absoluteRight.linkTo(heightAbilitiesDividerRef.absoluteLeft)
                    top.linkTo(parent.top)
                }
                .fillMaxHeight(), pokemon
        )
        VerticalDivider(
            modifier = Modifier.constrainAs(heightAbilitiesDividerRef) {
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(heightConstraintLayoutRef.absoluteRight)
                absoluteRight.linkTo(abilitiesConstraintLayout.absoluteLeft)
                top.linkTo(parent.top)
            }
        )
        AbilitiesConstraintLayout(
            modifier = Modifier.constrainAs(abilitiesConstraintLayout) {
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(heightAbilitiesDividerRef.absoluteRight)
                absoluteRight.linkTo(parent.absoluteRight)
                top.linkTo(parent.top)
            }, pokemon
        )
    }
}

@Composable
fun WeightConstraintLayout(modifier: Modifier, pokemon: Pokemon) {
    ConstraintLayout(modifier = modifier) {
        val (
            weightImageRef,
            pokemonInfoWeightTextRef,
            weightLabelTextRef,
        ) = createRefs()

        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.weight),
            modifier = Modifier
                .constrainAs(weightImageRef) {
                    bottom.linkTo(weightLabelTextRef.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(pokemonInfoWeightTextRef.absoluteLeft)
                    top.linkTo(parent.top)
                }
                .size(25.dp)
                .padding(bottom = 10.dp, end = 10.dp),
            contentDescription = "weight image"
        )
        Text(
            text = "${(pokemon.weight).toFloat() / 10} kg",
            modifier = Modifier
                .constrainAs(pokemonInfoWeightTextRef) {
                    bottom.linkTo(weightLabelTextRef.top)
                    absoluteLeft.linkTo(weightImageRef.absoluteRight)
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(parent.top)
                }
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Weight",
            modifier = Modifier
                .constrainAs(weightLabelTextRef) {
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}

@Composable
fun HeightConstraintLayout(modifier: Modifier, pokemon: Pokemon) {
    ConstraintLayout(modifier = modifier) {
        val (
            heightImageRef,
            pokemonInfoHeightTextRef,
            heightLabelTextRef,
        ) = createRefs()

        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.straighten),
            modifier = Modifier
                .constrainAs(heightImageRef) {
                    bottom.linkTo(heightLabelTextRef.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(pokemonInfoHeightTextRef.absoluteLeft)
                    top.linkTo(parent.top)
                }
                .size(25.dp)
                .padding(bottom = 10.dp, end = 10.dp),
            contentDescription = "height image"
        )
        Text(
            text = "${(pokemon.height).toFloat() / 10} m",
            modifier = Modifier
                .constrainAs(pokemonInfoHeightTextRef) {
                    bottom.linkTo(heightLabelTextRef.top)
                    absoluteLeft.linkTo(heightImageRef.absoluteRight)
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(parent.top)
                }
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Height",
            modifier = Modifier
                .constrainAs(heightLabelTextRef) {
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}

@Composable
fun AbilitiesConstraintLayout(modifier: Modifier, pokemon: Pokemon) {
    var abilityNames = ""
    for (i in pokemon.abilities.indices) {
        abilityNames += pokemon.abilities[i]
            .replaceFirstChar { it.uppercase() }
        if (i != pokemon.abilities.size - 1) abilityNames += "\n"
    }
    Log.d("mydebug", abilityNames)

    ConstraintLayout(modifier = modifier) {
        val (pokemonInfoAbilitiesTextRef, abilitiesLabelTextRef) = createRefs()

        Text(
            text = abilityNames,
            modifier = Modifier
                .constrainAs(pokemonInfoAbilitiesTextRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(abilitiesLabelTextRef.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                }
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Abilities",
            modifier = Modifier
                .constrainAs(abilitiesLabelTextRef) {
                    top.linkTo(pokemonInfoAbilitiesTextRef.bottom)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}

@Composable
fun AboutCardContent(pokemon: Pokemon) {
    with(pokemon) {
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
                        Log.d("mydebug", parent.toString())
                    }
                    .padding(top = 50.dp)
            ) {
                itemsIndexed(pokemon.types) { index, type ->
                    TypeElement(type, colorResource(getColor(pokemon.types[index])))
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
                    },
                pokemon = pokemon
            )
            Text(
                text = pokemon.flavor,
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

@Composable
fun AboutCard(modifier: Modifier, pokemon: Pokemon) {
    Card(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 230.dp,
                    bottom = 8.dp
                )
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        AboutCardContent(pokemon)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonInfo(pokemon: Pokemon) {
    val mainColor = colorResource(getColor(pokemon.types[0]))
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor)
    ) {
        val (
            pokeballImageRef,
            pokemonInfoHeaderRef,
            avatarImageRef,
            aboutCardRef
        ) = createRefs()

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(pokemonInfoHeaderRef) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    top.linkTo(parent.top)
                }
                .padding(start = 30.dp, end = 20.dp, top = 30.dp)
        ) {
            val (pokemonNameRef, pokemonIdRef) = createRefs()
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                modifier = Modifier
                    .constrainAs(pokemonNameRef) {
                        top.linkTo(parent.top)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                    },
                fontWeight = FontWeight.Bold,
                fontSize = 33.sp,
                color = Color.White
            )
            Text(
                text = "#" + get3digitValue(value = pokemon.id),
                modifier = Modifier
                    .constrainAs(pokemonIdRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        absoluteRight.linkTo(parent.absoluteRight)
                    },
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.White
            )
        }

        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.pokeball20),
            modifier = Modifier
                .size(250.dp)
                .constrainAs(pokeballImageRef) {
                    top.linkTo(parent.top)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
            contentDescription = "Background pokeball"
        )

        AboutCard(
            modifier = Modifier.constrainAs(aboutCardRef) {
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(parent.absoluteLeft)

                top.linkTo(parent.top)
            },
            pokemon = pokemon
        )

        GlideImage(
            model = pokemon.bigSprite,
            contentDescription = "pokemon avatar",
            modifier = Modifier
                .size(210.dp)
                .constrainAs(avatarImageRef) {
                    centerTo(parent)
                    verticalBias = 0.15f
                }
        )
    }
}

@Composable
fun PokemonInfoLoadingConstraintLayout(
    name: String,
    navController: NavController
) {
    val pokemonInfoViewModel = PokemonInfoViewModel(name = name)
    Text("загрузка")

    val coroutineScope = rememberCoroutineScope()
    val pokemonItemState = remember { mutableStateOf(null) }
    LaunchedEffect(key1 = Unit) {
        pokemonInfoViewModel.pokemonStateFlow.collect { pokemon ->
            if (pokemon != null) {
                val gson = Gson()
                val pokemonJson = gson.toJson(pokemon)

                val encodedPokemonJson = Uri.encode(pokemonJson)

                navController.navigate("PokemonInfo/${encodedPokemonJson}")
            }
        }
    }
}

