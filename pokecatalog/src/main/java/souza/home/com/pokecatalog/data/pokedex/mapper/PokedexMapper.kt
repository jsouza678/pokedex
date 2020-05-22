package souza.home.com.pokecatalog.data.pokedex.mapper

import souza.home.com.pokecatalog.data.pokedex.remote.model.PokeRootResponse
import souza.home.com.pokecatalog.utils.cropPokeUrl

class PokedexMapper {

    companion object {

        fun pokemonResponseAsDatabaseModel(pokeRootProperty: PokeRootResponse): Array<souza.home.com.pokecatalog.data.pokedex.local.entities.PokemonEntity>? {
            return pokeRootProperty.results?.map {
                souza.home.com.pokecatalog.data.pokedex.local.entities.PokemonEntity(
                    _id = Integer.parseInt(cropPokeUrl(it.id)),
                    name = it.name
                )
            }?.toTypedArray()
        }


        fun pokemonEntityAsDomainModel(pokemonEntity: List<souza.home.com.pokecatalog.data.pokedex.local.entities.PokemonEntity>?): List<souza.home.com.pokecatalog.domain.model.Pokemon>? {
            return pokemonEntity?.map {
                souza.home.com.pokecatalog.domain.model.Pokemon(
                    id = it._id,
                    name = it.name
                )
            }
        }
    }
}
