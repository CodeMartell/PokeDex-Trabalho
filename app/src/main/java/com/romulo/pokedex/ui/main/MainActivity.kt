package com.romulo.pokedex.ui.main
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.romulo.pokedex.data.PokemonRepository
import com.romulo.pokedex.data.remote.RetrofitModule
import com.romulo.pokedex.databinding.ActivityMainBinding
import com.romulo.pokedex.ui.detail.DetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        MainVMFactory(PokemonRepository(RetrofitModule.api))
    }

    private val adapter = PokemonAdapter { selected ->
        startActivity(
            Intent(this, DetailActivity::class.java)
                .putExtra("id", selected.id)
                .putExtra("name", selected.name)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView
        binding.rvPokemon.layoutManager = LinearLayoutManager(this)
        binding.rvPokemon.adapter = adapter

        viewModel.typeOptions.observe(this) { options ->
            binding.spnType.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        }

        viewModel.generationOptions.observe(this) { options ->
            binding.spnGeneration.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        }

        // Observa lista
        viewModel.pokemonList.observe(this) { list ->
            adapter.submitList(list)
            if (list.isNotEmpty()) {
                android.widget.Toast.makeText(
                    this,
                    "Carregados ${list.size} Pokémons",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Observa loading
        viewModel.loading.observe(this) { isLoading ->
            binding.edtSearch.isEnabled = !isLoading
            binding.spnType.isEnabled = !isLoading
            binding.spnGeneration.isEnabled = !isLoading
        }

        // Observa erro
        viewModel.error.observe(this) { msg ->
            if (!msg.isNullOrBlank()) {
                android.widget.Toast.makeText(
                    this,
                    "Erro: $msg",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }

        // Filtros
        val applyFilters = {
            viewModel.filter(
                binding.edtSearch.text?.toString().orEmpty(),
                (binding.spnType.selectedItem as? FilterOption)?.value,
                (binding.spnGeneration.selectedItem as? FilterOption)?.value
            )
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilters()
            }
        })

        binding.spnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p: AdapterView<*>?,
                v: android.view.View?,
                pos: Int,
                id: Long
            ) = applyFilters()

            override fun onNothingSelected(p: AdapterView<*>?) = applyFilters()
        }

        binding.spnGeneration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p: AdapterView<*>?,
                v: android.view.View?,
                pos: Int,
                id: Long
            ) = applyFilters()

            override fun onNothingSelected(p: AdapterView<*>?) = applyFilters()
        }

        // Carrega dados da API
        viewModel.load()
    }
}
