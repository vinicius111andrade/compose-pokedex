package com.vdemelo.mycomposepokedex.data.remote.responses.pokemon

data class Ability(
    val ability: AbilityX,
    val is_hidden: Boolean,
    val slot: Int
)