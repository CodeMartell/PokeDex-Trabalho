package com.romulo.pokedex.ui.main

data class FilterOption(val label: String, val value: String?) {
    override fun toString(): String = label
}
