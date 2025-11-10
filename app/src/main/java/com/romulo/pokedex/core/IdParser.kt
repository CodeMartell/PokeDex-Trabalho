package com.romulo.pokedex.core

// Extrai o ID a partir da URL da PokeAPI (ex.: .../pokemon/25/)
fun extractIdFromUrl(url: String): Int {
    val parts = url.trimEnd('/').split("/")
    return parts.last().toInt()
}
fun frontSpriteUrl(id: Int): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"