package org.bagolysz.rickmortywiki.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Character data as received from the server.
 */
@Entity
data class Character(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)