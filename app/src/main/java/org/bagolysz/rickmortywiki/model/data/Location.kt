package org.bagolysz.rickmortywiki.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Location data class as received from the server.
 */
@Entity
data class Location(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
)