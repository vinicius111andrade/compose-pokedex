package com.vdemelo.mycomposepokedex.data.remote.responses.pokemon

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)