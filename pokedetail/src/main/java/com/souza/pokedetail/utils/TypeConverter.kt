package com.souza.pokedetail.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.souza.pokedetail.data.pokedex.remote.model.ability.AbilitiesRoot
import com.souza.pokedetail.data.pokedex.remote.model.evolutionchain.EvolutionPath
import com.souza.pokedetail.data.pokedex.remote.model.stat.Sprites
import com.souza.pokedetail.data.pokedex.remote.model.stat.StatsRoot
import com.souza.pokedetail.data.pokedex.remote.model.type.TypeRoot
import com.souza.pokedetail.data.pokedex.remote.model.variety.Color
import com.souza.pokedetail.data.pokedex.remote.model.variety.Varieties
import com.souza.pokedetail.data.pokedex.remote.response.TypeResponse

class TypeConverter {

    companion object {
        private var gson = Gson()

        @TypeConverter
        fun fromEvolution(evolution: MutableList<String>?): String? {
            if (evolution == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<String>>() {
            }.type

            return gson.toJson(evolution, type)
        }

        @TypeConverter
        fun fromAbility(pokeAbilities: MutableList<AbilitiesRoot>?): String? {
            if (pokeAbilities == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<AbilitiesRoot>>() {
            }.type

            return gson.toJson(pokeAbilities, type)
        }

        @TypeConverter
        fun fromSprites(pokeSprites: Sprites?): String? {
            if (pokeSprites == null) { return null }
            gson = Gson()
            val type = object : TypeToken<Sprites>() {
            }.type

            return gson.toJson(pokeSprites, type)
        }

        @TypeConverter
        fun fromStats(pokeStats: List<StatsRoot>?): String? {
            if (pokeStats == null) { return null }
            gson = Gson()
            val type = object : TypeToken<List<StatsRoot>>() {
            }.type

            return gson.toJson(pokeStats, type)
        }

        @TypeConverter
        fun fromTypes(pokeTypes: MutableList<TypeRoot>?): String? {
            if (pokeTypes == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<TypeRoot>>() {
            }.type

            return gson.toJson(pokeTypes, type)
        }

        @TypeConverter
        fun fromTypesResponse(pokeTypesResponse: MutableList<TypeResponse>?): String? {
            if (pokeTypesResponse == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<TypeResponse>>() {
            }.type

            return gson.toJson(pokeTypesResponse, type)
        }

        @TypeConverter
        fun fromVarieties(pokeVarieties: MutableList<Varieties>?): String? {
            if (pokeVarieties == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<Varieties>>() {
            }.type

            return gson.toJson(pokeVarieties, type)
        }

        @TypeConverter
        fun fromColor(pokeColor: Color?): String? {
            if (pokeColor == null) { return null }
            gson = Gson()
            val type = object : TypeToken<Color>() {
            }.type

            return gson.toJson(pokeColor, type)
        }

        @TypeConverter
        fun fromEvolutionPath(evolutionChain: EvolutionPath?): String? {
            if (evolutionChain == null) { return null }
            gson = Gson()
            val type = object : TypeToken<EvolutionPath>() {
            }.type

            return gson.toJson(evolutionChain, type)
        }

        @TypeConverter
        fun toVarietiesList(pokeVarieties: String?): MutableList<Varieties>? {
            if (pokeVarieties == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<Varieties>>() {
            }.type

            return gson.fromJson(pokeVarieties, type)
        }

        @TypeConverter
        fun toColor(color: String?): Color? {
            if (color == null) { return null }
            gson = Gson()
            val type = object : TypeToken<Color>() {
            }.type

            return gson.fromJson(color, type)
        }

        @TypeConverter
        fun toEvolutionPath(evolutionPath: String?): EvolutionPath? {
            if (evolutionPath == null) { return null }
            gson = Gson()
            val type = object : TypeToken<EvolutionPath>() {
            }.type

            return gson.fromJson(evolutionPath, type)
        }

        @TypeConverter
        fun toAbilitiesList(pokeAbilities: String?): MutableList<AbilitiesRoot>? {
            if (pokeAbilities == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<AbilitiesRoot>>() {
            }.type

            return gson.fromJson(pokeAbilities, type)
        }

        @TypeConverter
        fun toSprites(pokeSprites: String?): Sprites? {
            if (pokeSprites == null) { return null }
            gson = Gson()
            val type = object : TypeToken<Sprites>() {
            }.type

            return gson.fromJson(pokeSprites, type)
        }

        @TypeConverter
        fun toStatsList(pokeStats: String?): List<StatsRoot>? {
            if (pokeStats == null) { return null }
            gson = Gson()
            val type = object : TypeToken<List<StatsRoot>>() {
            }.type

            return gson.fromJson(pokeStats, type)
        }

        @TypeConverter
        fun toTypesList(pokeTypes: String?): MutableList<TypeRoot>? {
            if (pokeTypes == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<TypeRoot>>() {
            }.type

            return gson.fromJson(pokeTypes, type)
        }

        @TypeConverter
        fun toTypesResponseList(pokeTypesResponse: String?): MutableList<TypeResponse>? {
            if (pokeTypesResponse == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<TypeResponse>>() {
            }.type

            return gson.fromJson(pokeTypesResponse, type)
        }

        @TypeConverter
        fun toEvolution(evolution: String?): MutableList<String>? {
            if (evolution == null) { return null }
            gson = Gson()
            val type = object : TypeToken<MutableList<String>>() {
            }.type

            return gson.fromJson(evolution, type)
        }
    }
}
