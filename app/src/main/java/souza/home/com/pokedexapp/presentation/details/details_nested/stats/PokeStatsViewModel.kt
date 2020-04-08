package souza.home.com.pokedexapp.presentation.details.details_nested.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import souza.home.com.pokedexapp.data.remote.PokeApi
import souza.home.com.pokedexapp.data.pokedex.remote.model.stats.PokemonProperty

enum class DetailsPokedexStatus{ LOADING, ERROR, DONE, EMPTY}

class PokeStatsViewModel(pokemon: String, app: Application): AndroidViewModel(app) {

    private var _status = MutableLiveData<DetailsPokedexStatus>()

    val status : LiveData<DetailsPokedexStatus>
        get() = _status

    private var _stats = MutableLiveData<PokemonProperty>()

    val stats : LiveData<PokemonProperty>
        get() = _stats

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        getStats(pokemon)
    }

    private fun getStats(pokemon: String) {

        _status.value = DetailsPokedexStatus.LOADING

        coroutineScope.launch {
            val getStatsDeferred = PokeApi.retrofitService.getPokeStats(pokemon)
            try{
                val listResult = getStatsDeferred.await()

                _stats.value = listResult
                _status.value = DetailsPokedexStatus.DONE

            }catch(t: Throwable){
                _status.value = DetailsPokedexStatus.ERROR
            }
        }
    }
}