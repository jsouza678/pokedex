package souza.home.com.pokedexapp.data.pokedex

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import souza.home.com.pokedexapp.data.pokedex.local.PokemonDatabase
import souza.home.com.pokedexapp.data.pokedex.mappers.PokedexMapper
import souza.home.com.pokedexapp.data.pokedex.remote.PokeApi
import souza.home.com.pokedexapp.domain.model.PokeEvolutionChain
import souza.home.com.pokedexapp.domain.repository.EvolutionRepository

class EvolutionRepositoryImpl(id: Int, context: Context) : EvolutionRepository {

    private val DB_INSTANCE = PokemonDatabase.getDatabase(context)

    override val evolution: LiveData<PokeEvolutionChain>? =
        DB_INSTANCE.evolutionChainDao.getEvolutionChain(id)?.let {
            Transformations.map(it){ evolutionObject ->
                evolutionObject?.let { it1 -> PokedexMapper.evolutionAsDomain(it1) }
            }
        }

    override suspend fun refreshEvolutionChain(id: Int) {
        withContext(Dispatchers.IO){
            try{
                val pokeEvolution = PokeApi.retrofitService.getEvolutionChain(id).await()
                DB_INSTANCE.evolutionChainDao.insertAll(PokedexMapper.evolutionChainToDatabaseModel(pokeEvolution))
            }catch(e: Exception){
                Log.i("Error" , "Message From Api on Evolution" + e.message)
            }
        }
    }
}