package com.souza.pokedetail.presentation.pokedetails.pokeattributes.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.souza.extensions.gone
import com.souza.extensions.observeOnce
import com.souza.extensions.visible
import com.souza.pokedetail.R
import com.souza.pokedetail.data.pokedex.remote.model.variety.Varieties
import com.souza.pokedetail.databinding.FragmentPokeAboutBinding
import com.souza.pokedetail.presentation.pokedetails.PokeDetailsFragment
import com.souza.pokedetail.utils.Constants.Companion.ABSOLUTE_ZERO
import com.souza.pokedetail.utils.Constants.Companion.EMPTY_STRING
import com.souza.pokedetail.utils.Constants.Companion.LIMIT_NORMAL_POKES
import com.souza.pokedetail.utils.cropPokeUrl
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AboutFragment(private val pokemonId: Int) : Fragment() {

    private val viewModel by viewModel<AboutViewModel> { parametersOf(pokemonId) }
    private lateinit var pokeVariationsSpinner: Spinner
    private lateinit var pokeDescriptionTextView: TextView
    private var pokemonList: MutableList<Varieties>? = mutableListOf()
    private val pokemonSpinnerAdapter by inject<AboutSpinnerAdapter> { parametersOf(pokemonList) }
    private lateinit var constraintDefault: ConstraintLayout
    private lateinit var constraintEvolution: ConstraintLayout
    private var uriEvolutionChain: Int = ABSOLUTE_ZERO
    private var urlEvolutionChain: String = EMPTY_STRING
    private var itemSelectedOnSpinner: Int = ABSOLUTE_ZERO
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPokeAboutBinding.inflate(layoutInflater)

        pokeDescriptionTextView = binding.textViewPokeDescAbout
        pokeVariationsSpinner = binding.spinnerVariationsAbout
        constraintDefault = binding.constraintLayoutDefaultAbout
        constraintEvolution = binding.constraintLayoutMysteriousAbout

        if (pokemonId > LIMIT_NORMAL_POKES) {
            constraintDefault.gone()
            constraintEvolution.visible()
        }

        initSpinner()
        initDataObserver()

        return binding.root
    }

    private fun initSpinner() {
        pokeVariationsSpinner.adapter = pokemonSpinnerAdapter
        pokeVariationsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                itemSelectedOnSpinner = position - 1
                when (position) {
                    ABSOLUTE_ZERO -> { } // Do Nothing. This is the hint position.
                    else -> { onSpinnerSelectedChange() }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun onSpinnerSelectedChange() {
        parseEvolutionPath()

        if (uriEvolutionChain == pokemonId) {
            turnOnSnackbarErrorPokeSelected()
        } else {
            turnOnSnackbarLoadingPokeSelected()
            val newPokeName = pokemonList?.get(itemSelectedOnSpinner)?.pokemon?.name
            val detailsFragment = newPokeName?.let { PokeDetailsFragment(
                pokemonId = uriEvolutionChain,
                pokemonName = it
            ) }

            detailsFragment?.let { changeFragment(fragment = it) }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.nav_host_fragment_details_activity, fragment)
            ?.commit()
    }

    private fun turnOnSnackbarErrorPokeSelected() {
        view?.let { Snackbar.make(it, getString(R.string.choose_another_poke_spinner_error), BaseTransientBottomBar.LENGTH_SHORT).show() }
    }

    private fun turnOnSnackbarLoadingPokeSelected() {
        view?.let { Snackbar.make(it, R.string.snackbar_loading_poke_evolution, BaseTransientBottomBar.LENGTH_SHORT).show() }
    }

    private fun parseEvolutionPath() {
        urlEvolutionChain = pokemonList?.get(itemSelectedOnSpinner)?.pokemon?.id!!
        uriEvolutionChain = Integer.parseInt(cropPokeUrl(urlEvolutionChain))
    }

    private fun initDataObserver() {
        viewModel.apply {
            this.updateVariationsOnViewLiveData()?.observeOnce(viewLifecycleOwner, Observer {
                pokemonSpinnerAdapter.submitList(it?.varieties)
                pokemonList = it?.varieties!!
                pokeDescriptionTextView.text = it.description
                setupOnClickPokeDescriptionDialog(it.description)
            })
        }
    }

    private fun setupOnClickPokeDescriptionDialog(message: String?) {
        pokeDescriptionTextView.setOnClickListener {
            setupPokeDescriptionDialog(message = message)
            openPokeDescriptionDialog()
        }
    }

    private fun setupPokeDescriptionDialog(message: String?) {
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.pokemon_description_dialog_pokemon_in_types))
            .setPositiveButton(getString(R.string.button_text_dismiss), null)
        materialAlertDialogBuilder.setMessage(message)
    }

    private fun openPokeDescriptionDialog() {
        materialAlertDialogBuilder.show()
    }
}
