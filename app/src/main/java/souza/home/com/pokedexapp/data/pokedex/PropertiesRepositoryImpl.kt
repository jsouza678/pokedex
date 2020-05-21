package souza.home.com.pokedexapp.data.pokedex

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import souza.home.com.pokedexapp.data.pokedex.local.PropertyDao
import souza.home.com.pokedexapp.data.pokedex.mapper.PokedexMapper
import souza.home.com.pokedexapp.data.pokedex.remote.PokedexService
import souza.home.com.pokedexapp.domain.model.PokeProperty
import souza.home.com.pokedexapp.domain.repository.PropertiesRepository

class PropertiesRepositoryImpl(
    private val pokedexService: PokedexService,
    private val propertyDao: PropertyDao
) : PropertiesRepository {

    override fun getProperties(id: Int): LiveData<PokeProperty>? {
        return propertyDao.getProperty(id)?.let {
            Transformations.map(it) { propertyObject ->
                propertyObject?.let { propertyItem -> PokedexMapper.propertyEntityAsDomainModel(propertyItem) }
            }
        }
    }

    override suspend fun refreshProperties(id: Int) {
        withContext(Dispatchers.IO) {
            val pokeProperty = pokedexService.fetchPokeStatsAsync(id).await()
            propertyDao.insertAll(PokedexMapper.propertiesResponseAsDatabaseModel(pokeProperty))
        }
    }
}
