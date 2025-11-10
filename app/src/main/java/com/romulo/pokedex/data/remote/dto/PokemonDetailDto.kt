package com.romulo.pokedex.data.remote.dto

import com.squareup.moshi.Json

data class PokemonDetailDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    // altura em decímetros / peso em hectogramas
    @Json(name = "height") val height: Int,
    @Json(name = "weight") val weight: Int,
    @Json(name = "types") val types: List<TypeSlotDto>,
    @Json(name = "stats") val stats: List<PokemonStatDto>,
    @Json(name = "sprites") val sprites: SpriteDto
)

data class TypeSlotDto(
    @Json(name = "slot") val slot: Int,
    @Json(name = "type") val type: TypeDto
)

data class TypeDto(
    @Json(name = "name") val name: String
)

data class PokemonStatDto(
    // 👇 IMPORTANTE: o JSON é "base_stat", mapeamos para "baseStat"
    @Json(name = "base_stat") val baseStat: Int,
    @Json(name = "stat") val stat: StatNameDto
)

data class StatNameDto(
    @Json(name = "name") val name: String // "hp", "attack", "defense", ...
)

data class SpriteDto(
    @Json(name = "front_default") val frontDefault: String?
)
