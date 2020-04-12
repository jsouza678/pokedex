package souza.home.com.pokedexapp.data.pokedex.remote.model.response

import com.squareup.moshi.JsonClass
import souza.home.com.pokedexapp.data.pokedex.remote.model.ability.AbilitiesMain
import souza.home.com.pokedexapp.data.pokedex.remote.model.stat.Sprites
import souza.home.com.pokedexapp.data.pokedex.remote.model.stat.Stats
import souza.home.com.pokedexapp.data.pokedex.remote.model.type.Types
import souza.home.com.pokedexapp.utils.Constants.Companion.EMPTY_STRING

@JsonClass(generateAdapter = true)
data class PropertyResponse(
    var id : Int = 0,
    var abilities : MutableList<AbilitiesMain>,
    var name: String = EMPTY_STRING,
    var height: Int = 0,
    var sprites: Sprites,
    var stats: List<Stats>,
    var types: MutableList<Types>,
    var weight: Int = 0
)