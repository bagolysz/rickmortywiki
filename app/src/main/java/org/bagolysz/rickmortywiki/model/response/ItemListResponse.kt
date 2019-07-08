package org.bagolysz.rickmortywiki.model.response

import org.bagolysz.rickmortywiki.model.data.Info

data class ItemListResponse<T>(
    val info: Info,
    val results: List<T>
)
