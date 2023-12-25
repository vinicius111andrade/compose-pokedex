package com.vdemelo.mycomposepokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vdemelo.mycomposepokedex.ui.pokemon.list.PokemonListScreen
import com.vdemelo.mycomposepokedex.ui.theme.MyComposePokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposePokedexTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "pokemon_list_screen"
                ) {
                    composable("pokemon_list_screen") {
                        PokemonListScreen(navController = navController)
                    }

                    composable(
                        route = "pokemon_detail_screen/{dominant_color}/{pokemon_name}",
                        arguments = listOf(
                            navArgument("dominant_color") {
                                type = NavType.IntType
                            },
                            navArgument("pokemon_name") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        //All composables that we want in our screen
                        val dominantColor = remember {
                            val colorInt = it.arguments?.getInt("dominant_color")
                            colorInt?.let { Color(it) } ?: Color.White
                        }
                        val pokemonName = remember {
                            it.arguments?.getString("pokemon_name")
                        }
                    }
                }
            }
        }
    }
}
