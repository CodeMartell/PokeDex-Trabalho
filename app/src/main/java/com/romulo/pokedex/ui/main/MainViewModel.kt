package com.romulo.pokedex.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romulo.pokedex.data.PokemonRepository
import com.romulo.pokedex.data.remote.dto.PokemonListItemDto
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.text.RegexOption

class MainViewModel(private val repo: PokemonRepository) : ViewModel() {

    private val _pokemonList = MutableLiveData<List<PokemonListItemDto>>()
    val pokemonList: LiveData<List<PokemonListItemDto>> = _pokemonList

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    private val _typeOptions = MutableLiveData<List<FilterOption>>()
    val typeOptions: LiveData<List<FilterOption>> = _typeOptions

    private val _generationOptions = MutableLiveData<List<FilterOption>>()
    val generationOptions: LiveData<List<FilterOption>> = _generationOptions

    private var allPokemons: List<PokemonListItemDto> = emptyList()
    private val typeCache = mutableMapOf<String, Set<String>>()
    private val generationCache = mutableMapOf<String, Set<String>>()
    private var filterJob: Job? = null

    fun load() {
        viewModelScope.launch {
            loading.postValue(true)
            error.postValue(null)
            try {
                val result = repo.getPokemonList()
                val typesResult = runCatching { repo.getAvailableTypes() }
                val generationsResult = runCatching { repo.getAvailableGenerations() }
                result.take(5).forEachIndexed { i, p ->
                    println("➡️ ${i + 1}. ${p.name} (id=${p.id})")
                }
                allPokemons = result
                _pokemonList.postValue(result)
                _typeOptions.postValue(buildTypeOptions(typesResult.getOrDefault(emptyList())))
                _generationOptions.postValue(buildGenerationOptions(generationsResult.getOrDefault(emptyList())))
                typesResult.exceptionOrNull()?.let { e ->
                    e.printStackTrace()
                    error.postValue(e.message ?: "Falha ao carregar tipos")
                }
                generationsResult.exceptionOrNull()?.let { e ->
                    e.printStackTrace()
                    error.postValue(e.message ?: "Falha ao carregar gerações")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                error.postValue(e.message ?: "Falha ao carregar Pokémons")
            } finally {
                loading.postValue(false)
            }
        }
    }

    fun filter(query: String, typeKey: String?, generationKey: String?) {
        if (allPokemons.isEmpty()) {
            _pokemonList.postValue(emptyList())
            return
        }
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            try {
                val q = query.trim().lowercase()
                val typeNames = typeKey?.let { key ->
                    typeCache[key] ?: run {
                        val names = repo.getPokemonNamesByType(key).toSet()
                        typeCache[key] = names
                        names
                    }
                }

                val generationNames = generationKey?.let { key ->
                    generationCache[key] ?: run {
                        val names = repo.getPokemonNamesByGeneration(key).toSet()
                        generationCache[key] = names
                        names
                    }
                }

                var filtered = allPokemons
                if (q.isNotEmpty()) filtered = filtered.filter { it.name.contains(q, ignoreCase = true) }
                if (typeNames != null) filtered = filtered.filter { typeNames.contains(it.name) }
                if (generationNames != null) filtered = filtered.filter { generationNames.contains(it.name) }

                _pokemonList.postValue(filtered)
            } catch (e: Exception) {
                e.printStackTrace()
                error.postValue(e.message ?: "Falha ao aplicar filtros")
            }
        }
    }

    private fun buildTypeOptions(typeNames: List<String>): List<FilterOption> {
        val sortedTypes = typeNames
            .filterNot { it.equals("unknown", ignoreCase = true) }
            .sortedBy { it.lowercase(Locale.ROOT) }
        val options = sortedTypes.map { name ->
            FilterOption(formatTypeLabel(name), name)
        }
        return listOf(FilterOption("Todos", null)) + options
    }

    private fun buildGenerationOptions(generationNames: List<String>): List<FilterOption> {
        val sortedGenerations = generationNames.sortedWith(
            compareBy({ generationSortIndex(it) }, { it.lowercase(Locale.ROOT) })
        )
        val options = sortedGenerations.map { name ->
            FilterOption(formatGenerationLabel(name), name)
        }
        return listOf(FilterOption("Todas", null)) + options
    }

    private fun formatTypeLabel(typeName: String): String {
        return typeName
            .split("-")
            .joinToString(" ") { part ->
                part.replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase(Locale.ROOT) else char.toString()
                }
            }
    }

    private fun formatGenerationLabel(generationName: String): String {
        val suffix = generationName.removePrefix("generation-")
        return if (suffix.matches(Regex("[ivx]+", RegexOption.IGNORE_CASE))) {
            "Gen ${suffix.uppercase(Locale.ROOT)}"
        } else {
            formatTypeLabel(generationName)
        }
    }

    private fun generationSortIndex(generationName: String): Int {
        val suffix = generationName.removePrefix("generation-")
        val roman = suffix.uppercase(Locale.ROOT)
        return ROMAN_TO_INT[roman] ?: Int.MAX_VALUE
    }

    companion object {
        private val ROMAN_TO_INT = mapOf(
            "I" to 1,
            "II" to 2,
            "III" to 3,
            "IV" to 4,
            "V" to 5,
            "VI" to 6,
            "VII" to 7,
            "VIII" to 8,
            "IX" to 9
        )
    }
}
