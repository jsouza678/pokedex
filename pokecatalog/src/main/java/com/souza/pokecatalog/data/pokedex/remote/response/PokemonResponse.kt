package com.souza.pokecatalog.data.pokedex.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse(
    @Json(name = "url")
    val id: String,
    val name: String
)
