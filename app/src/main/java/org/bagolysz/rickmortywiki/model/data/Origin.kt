package org.bagolysz.rickmortywiki.model.data

import androidx.room.Entity

@Entity
data class Origin(
    val name: String,
    val url: String
)