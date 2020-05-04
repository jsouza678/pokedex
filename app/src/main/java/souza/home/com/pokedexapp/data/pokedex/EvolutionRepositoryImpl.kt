package souza.home.com.pokedexapp.data.pokedex

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import souza.home.com.pokedexapp.R
import souza.home.com.pokedexapp.data.pokedex.local.EvolutionChainDao
import souza.home.com.pokedexapp.data.pokedex.mapper.PokedexMapper
import souza.home.com.pokedexapp.data.pokedex.remote.PokedexService
import souza.home.com.pokedexapp.domain.model.PokeEvolutionChain
import souza.home.com.pokedexapp.domain.repository.EvolutionRepository
import souza.home.com.pokedexapp.utils.CheckNetworkState

class EvolutionRepositoryImpl(private val context: Context,
                              private val evolutionChainDao: EvolutionChainDao,
                              private val pokedexService: PokedexService
) : EvolutionRepository {

    private val _internet = MutableLiveData<EvolutionPokedexStatus>()

    override fun getEvolutionChain(id: Int): LiveData<PokeEvolutionChain>? {
        return evolutionChainDao.getEvolutionChain(id)?.let {
            Transformations.map(it) { evolutionObject ->
                evolutionObject?.let { evolutionItem -> PokedexMapper.evolutionAsDomain(evolutionItem) }
            }
        }
    }

    override suspend fun refreshEvolutionChain(id: Int) {
        withContext(Dispatchers.IO) {
            if (CheckNetworkState.checkNetworkState(context)) {
                _internet.postValue(EvolutionPokedexStatus.LOADING)
                try {
                    val pokeEvolution = pokedexService.getEvolutionChain(id).await()
                    evolutionChainDao.insertAll(PokedexMapper.evolutionChainToDatabaseModel(pokeEvolution))
                    _internet.postValue(EvolutionPokedexStatus.DONE)
                } catch (e: Exception) {
                    _internet.postValue(EvolutionPokedexStatus.ERROR)
                    Log.i(context.getString(R.string.error_message_log), context.getString(R.string.error_log_evolution) + e.message)
                }
            } else {
                _internet.postValue(EvolutionPokedexStatus.ERROR)
            }
        }
    }
}

enum class EvolutionPokedexStatus { LOADING, ERROR, DONE }
