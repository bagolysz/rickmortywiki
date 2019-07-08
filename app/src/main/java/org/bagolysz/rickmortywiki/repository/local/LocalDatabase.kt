package org.bagolysz.rickmortywiki.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.bagolysz.rickmortywiki.model.data.Character

@Database(entities = arrayOf(Character::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}