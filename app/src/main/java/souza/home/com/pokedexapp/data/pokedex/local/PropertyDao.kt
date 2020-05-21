package souza.home.com.pokedexapp.data.pokedex.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import souza.home.com.pokedexapp.data.pokedex.local.entities.PropertyEntity
import souza.home.com.pokedexapp.utils.Constants.Companion.PROPERTY_TABLE_NAME

@Dao
interface PropertyDao {

    @Query("SELECT * FROM $PROPERTY_TABLE_NAME WHERE $PROPERTY_TABLE_NAME._id = :id")
    fun getProperty(id: Int): LiveData<PropertyEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg pokes: PropertyEntity)
}
