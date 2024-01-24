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
            val pokemonList = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)

            when (pokemonList) {
                is Resource.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= pokemonList.data!!.count
                    val pokedexEntries = pokemonList.data.results.map { it.toPokedexListEntry() }
                    currentPage++

                    loadError.value = ""
                    isLoading.value = false
                    this@PokemonListViewModel.pokemonList.value += pokedexEntries
                }

                is Resource.Error -> {
                    loadError.value = pokemonList.message!!
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

    private fun Result.toPokedexListEntry(): PokedexListEntry {
        val pokemonNumber = getPokemonNumber(this)
        val spriteUrl = getPokemonSpriteUrl(pokemonNumber)
        return PokedexListEntry(
            this.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            spriteUrl,
            pokemonNumber.toInt()
        )
    }

    private fun getPokemonNumber(result: Result): String {
        val charsToBeDropped = if (result.url.endsWith("/")) 1 else 0
        return result.url.dropLast(charsToBeDropped).takeLastWhile { it.isDigit() }
    }

    private fun getPokemonSpriteUrl(pokemonNumber: String): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemonNumber}.png"
    }

}
