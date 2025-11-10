package com.romulo.pokedex.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.romulo.pokedex.core.artworkUrlFromId
import com.romulo.pokedex.data.remote.dto.PokemonListItemDto
import com.romulo.pokedex.databinding.ItemPokemonBinding

class PokemonAdapter(
    private val onClick: (PokemonListItemDto) -> Unit
) : ListAdapter<PokemonListItemDto, PokemonAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<PokemonListItemDto>() {
        override fun areItemsTheSame(a: PokemonListItemDto, b: PokemonListItemDto) = a.id == b.id
        override fun areContentsTheSame(a: PokemonListItemDto, b: PokemonListItemDto) = a == b
    }

    inner class VH(val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            imgIcon.load(artworkUrlFromId(item.id))
            txtName.text = item.name.replaceFirstChar { it.titlecase() }
            txtId.text = "#${item.id}"
            root.setOnClickListener { onClick(item) }
        }
    }
}
