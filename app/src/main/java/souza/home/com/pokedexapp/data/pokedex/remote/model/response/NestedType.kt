package souza.home.com.pokedexapp.data.pokedex.remote.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NestedType(
    var pokemon: PokemonResponse
)