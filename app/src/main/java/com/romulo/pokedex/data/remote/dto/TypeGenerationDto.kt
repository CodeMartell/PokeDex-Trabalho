package com.romulo.pokedex.data.remote.dto

import com.squareup.moshi.Json

data class TypeDetailDto(
    @Json(name = "pokemon") val pokemon: List<TypePokemonSlotDto>
)

data class TypePokemonSlotDto(
    @Json(name = "pokemon") val pokemon: NamedResourceDto
)

data class GenerationDto(
    @Json(name = "pokemon_species") val species: List<NamedResourceDto>
)

data class NamedResourceDto(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)

data class NamedResourceListDto(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val results: List<NamedResourceDto>
)
