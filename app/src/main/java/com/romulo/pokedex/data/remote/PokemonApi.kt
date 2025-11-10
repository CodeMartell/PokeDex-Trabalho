package com.romulo.pokedex.data.remote

import com.romulo.pokedex.data.remote.dto.GenerationDto
import com.romulo.pokedex.data.remote.dto.NamedResourceListDto
import com.romulo.pokedex.data.remote.dto.PokemonDetailDto
import com.romulo.pokedex.data.remote.dto.PokemonListResponseDto
import com.romulo.pokedex.data.remote.dto.TypeDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    /**
     * 🔹 Endpoint para listar Pokémons
     * Exemplo: https://pokeapi.co/api/v2/pokemon?limit=100
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): PokemonListResponseDto

    /**
     * 🔹 Endpoint para detalhes de um Pokémon
     * Exemplo: https://pokeapi.co/api/v2/pokemon/{idOrName}
     */
    @GET("pokemon/{idOrName}")
    suspend fun getPokemonDetail(
        @Path("idOrName") idOrName: String
    ): PokemonDetailDto

    /**
     * 🔹 Endpoint para listar pokémons por tipo
     * Exemplo: https://pokeapi.co/api/v2/type/{typeName}
     */
    @GET("type/{typeName}")
    suspend fun getTypeDetail(
        @Path("typeName") typeName: String
    ): TypeDetailDto

    /**
     * 🔹 Endpoint para listar pokémons por geração
     * Exemplo: https://pokeapi.co/api/v2/generation/{generationName}
     */
    @GET("generation/{generationName}")
    suspend fun getGenerationDetail(
        @Path("generationName") generationName: String
    ): GenerationDto

    /**
     * 🔹 Endpoint para listar tipos disponíveis
     * Exemplo: https://pokeapi.co/api/v2/type
     */
    @GET("type")
    suspend fun getTypeList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): NamedResourceListDto

    /**
     * 🔹 Endpoint para listar gerações disponíveis
     * Exemplo: https://pokeapi.co/api/v2/generation
     */
    @GET("generation")
    suspend fun getGenerationList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): NamedResourceListDto
}
