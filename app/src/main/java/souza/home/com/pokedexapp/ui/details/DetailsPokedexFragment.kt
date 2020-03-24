package souza.home.com.pokedexapp.ui.details


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import souza.home.com.pokedexapp.R


/**
 * A simple [Fragment] subclass.
 */
class DetailsPokedexFragment : Fragment() {


    private lateinit var tvName : TextView
    private lateinit var lvStats : ListView
    private lateinit var lvTypes : ListView
    private lateinit var lvChain : ListView
    private lateinit var spVariations : Spinner
    private lateinit var evolutionArray: ArrayList<String>
    private lateinit var varietiesArray: ArrayList<String>

    private val viewModel: DetailsPokedexViewModel by lazy{
        ViewModelProviders.of(this).get(DetailsPokedexViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_details_pokedex, container, false)
        val poke: String = "25"

        evolutionArray = ArrayList()
        varietiesArray = ArrayList()
        tvName = view.findViewById(R.id.tv_detail_name)
        lvStats = view.findViewById(R.id.lv_stats)
        lvTypes = view.findViewById(R.id.lv_types)
        lvChain = view.findViewById(R.id.lv_chain)
        spVariations = view.findViewById(R.id.spinner_variations)


        viewModel.getStats(poke, view.context, tvName, lvStats)
        viewModel.getChainEvolution(poke, view.context, evolutionArray, lvChain)
        viewModel.getVarieties(poke, view.context, varietiesArray, spVariations, evolutionArray, lvChain, tvName, lvStats)

        return view
    }



}
