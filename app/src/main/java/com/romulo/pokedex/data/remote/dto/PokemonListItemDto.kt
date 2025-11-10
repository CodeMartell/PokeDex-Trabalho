package com.romulo.pokedex.data.remote.dto

import com.squareup.moshi.Json

data class PokemonListResponseDto(
    @field:Json(name = "count") val count: Int,
    @field:Json(name = "next") val next: String?,
    @field:Json(name = "results") val results: List<PokemonListItemDto>
)

data class PokemonListItemDto(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "url") val url: String
) {
    // Extrai o ID do final da URL (ex: .../pokemon/25/ -> 25)
    val id: Int
        get() = url.trimEnd('/').split("/").last().toInt()
}
