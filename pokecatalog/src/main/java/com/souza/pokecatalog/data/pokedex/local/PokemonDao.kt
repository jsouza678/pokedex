package com.souza.pokecatalog.data.pokedex.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.souza.pokecatalog.data.pokedex.local.entities.PokemonEntity
import com.souza.pokecatalog.utils.Constants.Companion.POKE_TABLE_NAME

@Dao
interface PokemonDao {

    @Query("SELECT * FROM $POKE_TABLE_NAME")
    fun getPokes(): LiveData<List<PokemonEntity>?>

    @Query("SELECT * FROM $POKE_TABLE_NAME WHERE $POKE_TABLE_NAME.name LIKE :pokeName||'%'")
    fun getPokesByName(pokeName: String): LiveData<List<PokemonEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg pokes: PokemonEntity)
}
