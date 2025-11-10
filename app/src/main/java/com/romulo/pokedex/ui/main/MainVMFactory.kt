package com.romulo.pokedex.ui.main

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romulo.pokedex.data.PokemonRepository

class MainVMFactory(private val repo: PokemonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(repo) as T
    }
}

/** Helper para evitar boilerplate dos spinners */
fun AdapterView<*>.setOnItemSelectedListenerCompat(onChanged: () -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?, view: View?, position: Int, id: Long
        ) = onChanged()
        override fun onNothingSelected(parent: AdapterView<*>?) = onChanged()
    }
}
