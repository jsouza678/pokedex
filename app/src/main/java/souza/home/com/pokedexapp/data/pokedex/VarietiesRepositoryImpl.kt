package souza.home.com.pokedexapp.data.pokedex

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import souza.home.com.pokedexapp.R
import souza.home.com.pokedexapp.data.pokedex.local.VarietiesDao
import souza.home.com.pokedexapp.data.pokedex.mapper.PokedexMapper
import souza.home.com.pokedexapp.data.pokedex.remote.PokedexService
import souza.home.com.pokedexapp.domain.model.PokeVariety
import souza.home.com.pokedexapp.domain.repository.VarietiesRepository
import souza.home.com.pokedexapp.utils.CheckNetworkState

class VarietiesRepositoryImpl(
    private val context: Context,
    private val varietiesDao: VarietiesDao,
    private val pokedexService: PokedexService
) : VarietiesRepository {

    private val _internet = MutableLiveData<VarietiesPokedexStatus>()

    val internet: LiveData<VarietiesPokedexStatus>
        get() = _internet

    override fun getVarieties(id: Int): LiveData<PokeVariety?>? {
        val varieties = Transformations.map(varietiesDao.getVariety(id)) {
            it?.let { varietiesItem -> PokedexMapper.variationsEntityAsDomainModel(varietiesItem) }
        }
        return varieties
    }

    override suspend fun refreshVarieties(id: Int) {
        withContext(Dispatchers.IO) {
            if (CheckNetworkState.checkNetworkState(context)) {
                _internet.postValue(VarietiesPokedexStatus.LOADING)
                try {
                    val pokeVariations = pokedexService.fetchVariationsAsync(id).await()
                    varietiesDao.insertAll(
                        PokedexMapper.variationsResponseAsDatabaseModel(
                            pokeVariations
                        )
                    )
                    if (pokeVariations._id.isEmpty()) {
                        _internet.postValue(VarietiesPokedexStatus.EMPTY)
                    } else {
                        _internet.postValue(VarietiesPokedexStatus.DONE)
                    }
                } catch (e: Exception) {
                    _internet.postValue(VarietiesPokedexStatus.ERROR)
                    Log.i(
                        context.getString(R.string.error_message_log),
                        context.getString(R.string.error_log_varieties) + e.message
                    )
                }
            } else {
                _internet.postValue(VarietiesPokedexStatus.ERROR)
            }
        }
    }
}

enum class VarietiesPokedexStatus { LOADING, ERROR, DONE, EMPTY }
