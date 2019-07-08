package org.bagolysz.rickmortywiki.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Episode information as received from the server.
 */
@Entity
data class Episode(
    @PrimaryKey val id: Int,
    val name: String,
    @SerializedName("air_date") val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)