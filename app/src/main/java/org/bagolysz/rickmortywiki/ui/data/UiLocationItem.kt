package org.bagolysz.rickmortywiki.ui.data

data class UiLocationItem(
    override val id: Int,
    override val name: String,
    val dimension: String,
    val population: Int,
    val type: String
) : BaseUiItem(id, name)