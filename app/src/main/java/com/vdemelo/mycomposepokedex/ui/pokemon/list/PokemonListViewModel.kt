package com.vdemelo.mycomposepokedex.ui.pokemon.list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.graphics.Palette
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdemelo.mycomposepokedex.data.models.PokedexListEntry
import com.vdemelo.mycomposepokedex.data.remote.responses.pokemonlist.Result
import com.vdemelo.mycomposepokedex.repository.PokemonRepository
import com.vdemelo.mycomposepokedex.util.Constants.PAGE_SIZE
import com.vdemelo.mycomposepokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var currentPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)

            when (result) {
                is Resource.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val pokemonNumber = getPokemonNumber(entry)
                        val spriteUrl = getPokemonSpriteUrl(pokemonNumber)
                        PokedexListEntry(
                            entry.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            spriteUrl,
                            pokemonNumber.toInt()
                        )
                    }
                    currentPage++

                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun calculateDominantColor(
        drawable: Drawable,
        onFinish: (Color) -> Unit
    ) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorInt ->
                onFinish(Color(colorInt))
            }
        }
    }

}

private fun getPokemonNumber(entry: Result): String {
    val charsToBeDropped = if (entry.url.endsWith("/")) 1 else 0
    return entry.url.dropLast(charsToBeDropped).takeLastWhile { it.isDigit() }
}

private fun getPokemonSpriteUrl(pokemonNumber: String): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemonNumber}.png"
}