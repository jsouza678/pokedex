package souza.home.com.pokedexapp.presentation.detailsfragment.details_nested.evolution_chain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import souza.home.com.extensions.observeOnce
import souza.home.com.pokedexapp.R
import souza.home.com.pokedexapp.data.pokedex.remote.model.evolution_chain.Evolution

class EvolutionChainFragment(private val pokemon: Int) : Fragment() {

    private lateinit var lvChain: ListView
    private var listString: MutableList<String> = mutableListOf()
    private val adapterChain by inject<EvolutionChainAdapter>{ parametersOf(listString) }
    private val viewModel by viewModel<EvolutionChainViewModel>{ parametersOf(pokemon)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_poke_chain, container, false)
        bindViews(view)
        initObserver()
        initChainEvolution()

        return view
    }

    private fun bindViews(view: View) {
        lvChain = view.findViewById(R.id.list_view_chain)
    }

    private fun initObserver() {
        viewModel.updateEvolutionOnViewLiveData()?.observeOnce(viewLifecycleOwner, Observer {
            listString = it.evolution!!
            adapterChain.submitList(listString)
        })
    }

    private fun initChainEvolution() {
        lvChain.adapter = adapterChain
    }
}
