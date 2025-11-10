package com.romulo.pokedex.data.remote

// === LISTA DE POKÉMONS ===
data class PokemonListResponse(
    val count: Int,
    val results: List<PokemonSimple>
)

data class PokemonSimple(
    val name: String,
    val url: String
)

// === DETALHE DO POKÉMON ===
// Campos usados: id, name, height, weight, types, stats
data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int, // em decímetros (dm)
    val weight: Int, // em hectogramas (hg)
    val types: List<TypeWrapper>,
    val stats: List<StatWrapper>
) {
    data class TypeWrapper(
        val slot: Int,
        val type: NamedAPIResource
    )

    data class StatWrapper(
        val base_stat: Int,
        val stat: NamedAPIResource
    )
}

data class NamedAPIResource(
    val name: String,
    val url: String
)

// === RESPOSTA POR TIPO (para filtro) ===
data class TypeResponse(
    val name: String,
    val pokemon: List<TypePokemonWrapper>
) {
    data class TypePokemonWrapper(
        val pokemon: NamedAPIResource
    )
}
