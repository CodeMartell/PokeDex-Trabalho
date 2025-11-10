package com.romulo.pokedex.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import coil.load
import com.google.android.material.chip.Chip
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.romulo.pokedex.R
import com.romulo.pokedex.core.artworkUrlFromId
import com.romulo.pokedex.data.PokemonRepository
import com.romulo.pokedex.data.remote.RetrofitModule
import com.romulo.pokedex.data.remote.dto.PokemonDetailDto
import com.romulo.pokedex.databinding.ActivityDetailBinding

class DetailActivity : ComponentActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel> {
        DetailVMFactory(PokemonRepository(RetrofitModule.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // recebemos id (preferencial) ou name
        val id = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name")

        when {
            id != -1 -> viewModel.load(id.toString())
            !name.isNullOrBlank() -> viewModel.load(name)
        }

        viewModel.detail.observe(this) { d -> d?.let { renderDetail(it) } }
        viewModel.error.observe(this) { msg ->
            if (!msg.isNullOrBlank()) binding.txtTitle.text = "Erro: $msg"
        }
    }

    private fun renderDetail(d: PokemonDetailDto) {
        binding.imgArtwork.load(artworkUrlFromId(d.id)) {
            crossfade(true)
        }

        val formattedName = d.name.replaceFirstChar { it.titlecase() }
        binding.txtTitle.text = getString(R.string.detail_title_format, formattedName, d.id)

        renderTypes(d)

        val heightM = d.height / 10.0
        val weightKg = d.weight / 10.0
        binding.txtHW.text = getString(R.string.detail_height_weight, heightM, weightKg)

        binding.txtStatsTitle.text = getString(R.string.detail_stats_title)
        renderStats(d)
    }

    private fun renderTypes(detail: PokemonDetailDto) {
        binding.txtTypesLabel.text = getString(R.string.detail_types_label)
        binding.chipTypes.removeAllViews()

        detail.types
            .sortedBy { it.slot }
            .map { it.type.name.replaceFirstChar { ch -> ch.titlecase() } }
            .forEach { typeName ->
                val chip = Chip(this).apply {
                    text = typeName
                    isCheckable = false
                    isClickable = false
                    setEnsureMinTouchTargetSize(false)
                    setTextColor(ContextCompat.getColor(context, R.color.type_chip_text))
                    setChipBackgroundColorResource(R.color.type_chip_background)
                }
                binding.chipTypes.addView(chip)
            }

        if (binding.chipTypes.childCount == 0) {
            val chip = Chip(this).apply {
                text = "—"
                isEnabled = false
            }
            binding.chipTypes.addView(chip)
        }
    }

    private fun renderStats(detail: PokemonDetailDto) {
        binding.containerStats.removeAllViews()
        val inflater = LayoutInflater.from(this)

        detail.stats.forEach { stat ->
            val row = inflater.inflate(R.layout.item_stat_row, binding.containerStats, false)
            val label = row.findViewById<TextView>(R.id.txtStatLabel)
            val value = row.findViewById<TextView>(R.id.txtStatValue)
            val progress = row.findViewById<LinearProgressIndicator>(R.id.progressStat)

            val statNameRes = STAT_LABELS[stat.stat.name]
            val statName = statNameRes?.let(::getString)
                ?: stat.stat.name.replaceFirstChar { it.titlecase() }
            label.text = statName
            value.text = stat.baseStat.toString()

            progress.max = MAX_STAT_VALUE
            progress.progress = stat.baseStat.coerceAtMost(MAX_STAT_VALUE)

            val indicatorColorRes = STAT_COLORS[stat.stat.name] ?: R.color.stat_default
            progress.setIndicatorColor(ContextCompat.getColor(this, indicatorColorRes))
            progress.trackColor = ContextCompat.getColor(this, R.color.stat_track)

            binding.containerStats.addView(row)
        }
    }

    companion object {
        private const val MAX_STAT_VALUE = 255

        private val STAT_LABELS = mapOf(
            "hp" to R.string.stat_hp,
            "attack" to R.string.stat_attack,
            "defense" to R.string.stat_defense,
            "special-attack" to R.string.stat_special_attack,
            "special-defense" to R.string.stat_special_defense,
            "speed" to R.string.stat_speed
        )

        private val STAT_COLORS = mapOf(
            "hp" to R.color.stat_hp,
            "attack" to R.color.stat_attack,
            "defense" to R.color.stat_defense,
            "special-attack" to R.color.stat_special_attack,
            "special-defense" to R.color.stat_special_defense,
            "speed" to R.color.stat_speed
        )
    }
}
