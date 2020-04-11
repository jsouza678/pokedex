package souza.home.com.pokedexapp.utils

import souza.home.com.pokedexapp.data.pokedex.remote.model.variety.PokeFlavorDescription
import souza.home.com.pokedexapp.utils.Constants.Companion.LANGUAGE_DESCRIPTIONS

fun cropPokeUrl(url: String) : String {
    val id = url.substringAfterLast("n/")?.substringBeforeLast("/")

    return id
}

fun optimizeDescription(it: MutableList<PokeFlavorDescription>?) : String{
    var description : String = ""

    for(i in 0 until it?.size!!) {
        if (it.get(i).language.name == LANGUAGE_DESCRIPTIONS) {
            description += it[i].flavor_text + "\n "
        }
    }

    return description
}