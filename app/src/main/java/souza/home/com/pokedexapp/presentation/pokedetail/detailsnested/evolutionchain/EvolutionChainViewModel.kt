package souza.home.com.pokedexapp.presentation.pokedetail.detailsnested.evolutionchain

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import souza.home.com.pokedexapp.domain.model.PokeEvolutionChain
import souza.home.com.pokedexapp.domain.usecase.GetEvolutionChainFromApi
import souza.home.com.pokedexapp.domain.usecase.GetEvolutionChainFromDatabase

class EvolutionChainViewModel(
    private val pokemon: Int,
    private val getEvolutionChainFromApi: GetEvolutionChainFromApi,
    private val getEvolutionChainFromDatabase: GetEvolutionChainFromDatabase
) : ViewModel() {

    fun updateEvolutionOnViewLiveData(): LiveData<PokeEvolutionChain>? = getEvolutionChainFromDatabase(pokemon)
    private val coroutineScope = Dispatchers.IO

    init {
        getChainEvolution(pokemon)
    }

    private fun getChainEvolution(chainId: Int) {
        viewModelScope.launch(coroutineScope) {
            getEvolutionChainFromApi(chainId)
        }
    }
}
