package org.bagolysz.rickmortywiki.ui.feature.character

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_character_detail.view.*
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.ui.data.UiCharacterDetailItem

class CharacterDetailsAdapter(context: Context) :
    RecyclerView.Adapter<CharacterDetailsAdapter.CharacterDetailViewHolder>() {

    private val dataList: MutableList<UiCharacterDetailItem> = mutableListOf()
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterDetailViewHolder {
        val view = layoutInflater.inflate(R.layout.list_item_character_detail, parent, false)
        return CharacterDetailViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CharacterDetailViewHolder, position: Int) {
        val item = dataList[position]
        holder.elementTextView.text = item.elementDescription
        holder.statusTextView.text = item.status
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setDataList(list: List<UiCharacterDetailItem>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    class CharacterDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val elementTextView: TextView = view.item_character_detail_element
        val statusTextView: TextView = view.item_character_detail_status
    }
}