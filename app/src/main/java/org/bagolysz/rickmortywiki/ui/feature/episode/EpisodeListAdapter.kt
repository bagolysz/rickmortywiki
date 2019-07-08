package org.bagolysz.rickmortywiki.ui.feature.episode

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_episode.view.*
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.ui.base.BaseAdapter
import org.bagolysz.rickmortywiki.ui.data.UiEpisodeItem

class EpisodeListAdapter(context: Context) : BaseAdapter<UiEpisodeItem>(context) {

    init {
        setHasStableIds(true)
    }

    override fun createDataViewHolderItem(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = layoutInflater.inflate(R.layout.list_item_episode, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun bindDataViewHolderItem(holder: RecyclerView.ViewHolder, item: UiEpisodeItem) {
        val itemHolder = holder as EpisodeViewHolder
        itemHolder.nameTextView.text = item.name
        itemHolder.episodeTextView.text = item.episode
        itemHolder.airDateTextView.text = item.airDate
    }

    class EpisodeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.item_episode_list_name_tv
        val episodeTextView: TextView = view.item_episode_list_episode_tv
        val airDateTextView: TextView = view.item_episode_list_air_date_tv
    }
}