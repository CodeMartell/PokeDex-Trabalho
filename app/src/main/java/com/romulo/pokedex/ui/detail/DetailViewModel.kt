package com.romulo.pokedex.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romulo.pokedex.data.PokemonRepository
import com.romulo.pokedex.data.remote.dto.PokemonDetailDto
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: PokemonRepository) : ViewModel() {
    val detail = MutableLiveData<PokemonDetailDto?>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    fun load(idOrName: String) {
        viewModelScope.launch {
            loading.postValue(true)
            error.postValue(null)
            try {
                detail.postValue(repo.getPokemonDetail(idOrName))
            } catch (e: Exception) {
                e.printStackTrace()
                error.postValue(e.message)
            } finally {
                loading.postValue(false)
            }
        }
    }
}
