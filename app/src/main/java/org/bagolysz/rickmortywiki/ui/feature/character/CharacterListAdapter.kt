package org.bagolysz.rickmortywiki.ui.feature.character

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_character.view.*
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.ui.base.BaseAdapter
import org.bagolysz.rickmortywiki.ui.data.UiCharacterItem
import org.bagolysz.rickmortywiki.util.GlideApp

class CharacterListAdapter(context: Context) : BaseAdapter<UiCharacterItem>(context) {

    init {
        setHasStableIds(true)
    }

    override fun createDataViewHolderItem(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = layoutInflater.inflate(R.layout.list_item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun bindDataViewHolderItem(holder: RecyclerView.ViewHolder, item: UiCharacterItem) {
        val charHolder = holder as CharacterViewHolder
        charHolder.nameTextView.text = item.name
        charHolder.statusTextView.text = item.status
        GlideApp.with(charHolder.view)
            .load(item.imageUrl)
            .into(charHolder.imageView)
    }

    class CharacterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.item_character_list_name_tv
        val statusTextView: TextView = view.item_character_list_status_tv
        val imageView: ImageView = view.item_character_list_image_view
    }
}