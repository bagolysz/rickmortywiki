package org.bagolysz.rickmortywiki.ui.data

data class UiEpisodeItem(
    override val id: Int,
    override val name: String,
    val airDate: String,
    val episode: String
) : BaseUiItem(id, name)