package org.bagolysz.rickmortywiki.ui.data

data class UiCharacterItem(
    override val id: Int,
    override val name: String,
    val status: String,
    val imageUrl: String
) : BaseUiItem(id, name)