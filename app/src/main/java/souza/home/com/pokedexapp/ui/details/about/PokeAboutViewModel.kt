package souza.home.com.pokedexapp.ui.details.about

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import souza.home.com.pokedexapp.network.PokeApi
import souza.home.com.pokedexapp.network.model.stats.PokemonProperty
import souza.home.com.pokedexapp.network.model.varieties.PokeRootVarieties


enum class DetailsPokedexStatus{ LOADING, ERROR, DONE, EMPTY}

class PokeAboutViewModel(pokemon: String, app: Application): AndroidViewModel(app) {

    private var _status = MutableLiveData<DetailsPokedexStatus>()

    val status : LiveData<DetailsPokedexStatus>
        get() = _status

    private var _varieties = MutableLiveData<PokeRootVarieties>()

    val varieties : LiveData<PokeRootVarieties>
        get() = _varieties

    init {
        getVarieties(pokemon)
    }

    private fun getVarieties(pokemon: String){

        _status.value = DetailsPokedexStatus.LOADING

        PokeApi.retrofitService.getVariations(pokemon).enqueue(object: Callback<PokeRootVarieties> {
            override fun onFailure(call: Call<PokeRootVarieties>, t: Throwable) {
                _status.value = DetailsPokedexStatus.ERROR
            }

            override fun onResponse(call: Call<PokeRootVarieties>, response: Response<PokeRootVarieties>) {
                val varietiesArray: ArrayList<String> = ArrayList()
                val items = response.body()
                val length = response.body()?.varieties?.size


                for(i in 0 until length!!) {
                    try {
                        varietiesArray.add(response.body()?.varieties!![i].pokemon.name.capitalize())
                        _status.value = DetailsPokedexStatus.DONE
                    } catch (e: Exception) {
                        // varietiesArray.add("No varieties")
                        _status.value = DetailsPokedexStatus.EMPTY
                    }
                }

                _varieties.value = items

            }
        }
        )
    }

}