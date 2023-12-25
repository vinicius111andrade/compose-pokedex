package com.vdemelo.mycomposepokedex.data.remote.responses

import com.vdemelo.mycomposepokedex.data.remote.responses.pokemon.Ability
import com.vdemelo.mycomposepokedex.data.remote.responses.pokemon.Form
import com.vdemelo.mycomposepokedex.data.remote.responses.pokemon.GameIndice
import com.vdemelo.mycomposepokedex.data.remote.responses.pokemon.Move
import com.vdemelo.mycomposepokedex.data.remote.responses.pokemon.Species
import com.vdemelo.mycomposepokedex.data.remote.responses.pokemon.Sprites
import com.vdemelo.mycomposepokedex.data.remote.responses.pokemon.Stat
import com.vdemelo.mycomposepokedex.data.remote.responses.pokemon.Type

data class Pokemon(
    val abilities: List<Ability>,
    val base_experience: Int,
    val forms: List<Form>,
    val game_indices: List<GameIndice>,
    val height: Int,
    val held_items: List<Any>,
    val id: Int,
    val is_default: Boolean,
    val location_area_encounters: String,
    val moves: List<Move>,
    val name: String,
    val order: Int,
    val past_abilities: List<Any>,
    val past_types: List<Any>,
    val species: Species,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int
)