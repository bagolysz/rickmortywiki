package org.bagolysz.rickmortywiki.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import org.bagolysz.rickmortywiki.model.data.Character

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    fun getAll(): Observable<List<Character>>

    @Insert
    fun insert(vararg characters: Character)
}