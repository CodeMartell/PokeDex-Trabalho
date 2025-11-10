package com.romulo.pokedex.data

import com.romulo.pokedex.data.remote.PokemonApi
import com.romulo.pokedex.data.remote.dto.PokemonDetailDto
import com.romulo.pokedex.data.remote.dto.PokemonListItemDto

class PokemonRepository(private val api: PokemonApi) {

    suspend fun getPokemonList(pageSize: Int = 200): List<PokemonListItemDto> {
        val accumulated = mutableListOf<PokemonListItemDto>()
        var offset = 0
        var hasNext: Boolean

        do {
            val response = api.getPokemonList(limit = pageSize, offset = offset)
            accumulated += response.results
            hasNext = response.next != null && response.results.isNotEmpty()
            offset += pageSize
        } while (hasNext)

        return accumulated
    }

    suspend fun getPokemonDetail(idOrName: String): PokemonDetailDto {
        return api.getPokemonDetail(idOrName)
    }

    suspend fun getPokemonNamesByType(typeName: String): List<String> {
        return api.getTypeDetail(typeName).pokemon.map { it.pokemon.name }
    }

    suspend fun getPokemonNamesByGeneration(generationName: String): List<String> {
        return api.getGenerationDetail(generationName).species.map { it.name }
    }

    suspend fun getAvailableTypes(): List<String> {
        return api.getTypeList(limit = 200).results.map { it.name }
    }

    suspend fun getAvailableGenerations(): List<String> {
        return api.getGenerationList(limit = 200).results.map { it.name }
    }
}
