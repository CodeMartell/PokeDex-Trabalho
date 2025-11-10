package com.romulo.pokedex.data

import com.romulo.pokedex.data.remote.PokemonApi
import com.romulo.pokedex.data.remote.dto.PokemonDetailDto
import com.romulo.pokedex.data.remote.dto.PokemonListItemDto

class PokemonRepository(private val api: PokemonApi) {

    suspend fun getPokemonList(pageSize: Int = 200): List<PokemonListItemDto> {
        println("🌍 Carregando lista completa da PokéAPI (pageSize=$pageSize)...")
        val accumulated = mutableListOf<PokemonListItemDto>()
        var offset = 0
        var hasNext: Boolean

        do {
            println("➡️  Buscando página (offset=$offset)")
            val response = api.getPokemonList(limit = pageSize, offset = offset)
            println("✅ Página retornou ${response.results.size} itens")
            accumulated += response.results
            hasNext = response.next != null && response.results.isNotEmpty()
            offset += pageSize
        } while (hasNext)

        println("📦 Total acumulado: ${accumulated.size} pokémons")
        return accumulated
    }

    suspend fun getPokemonDetail(idOrName: String): PokemonDetailDto {
        println("🔍 Buscando detalhes de $idOrName...")
        return api.getPokemonDetail(idOrName)
    }

    suspend fun getPokemonNamesByType(typeName: String): List<String> {
        println("🧪 Carregando pokémons do tipo $typeName")
        return api.getTypeDetail(typeName).pokemon.map { it.pokemon.name }
    }

    suspend fun getPokemonNamesByGeneration(generationName: String): List<String> {
        println("🗺️  Carregando pokémons da geração $generationName")
        return api.getGenerationDetail(generationName).species.map { it.name }
    }

    suspend fun getAvailableTypes(): List<String> {
        println("📚 Carregando lista de tipos disponíveis")
        return api.getTypeList(limit = 200).results.map { it.name }
    }

    suspend fun getAvailableGenerations(): List<String> {
        println("🧭 Carregando lista de gerações disponíveis")
        return api.getGenerationList(limit = 200).results.map { it.name }
    }
}
