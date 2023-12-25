package com.vdemelo.mycomposepokedex.data.remote.responses

import com.vdemelo.mycomposepokedex.data.remote.responses.pokemonlist.Result

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)