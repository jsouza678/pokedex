package com.souza.pokecatalog.utils

fun cropPokeUrl(url: String): String {
    return url.substringAfterLast("n/").substringBeforeLast("/")
}
