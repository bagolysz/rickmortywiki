package org.bagolysz.rickmortywiki.model.data

/**
 * Contains pagination info received from the server.
 */
data class Info(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String
)