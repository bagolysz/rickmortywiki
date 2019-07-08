package org.bagolysz.rickmortywiki.ui.feature.location

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_location.view.*
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.ui.base.BaseAdapter
import org.bagolysz.rickmortywiki.ui.data.UiLocationItem

class LocationListAdapter(context: Context) : BaseAdapter<UiLocationItem>(context) {

    init {
        setHasStableIds(true)
    }

    override fun createDataViewHolderItem(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = layoutInflater.inflate(R.layout.list_item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun bindDataViewHolderItem(holder: RecyclerView.ViewHolder, item: UiLocationItem) {
        val locHolder = holder as LocationViewHolder
        locHolder.nameTextView.text = item.name
        locHolder.dimensionTextView.text = item.dimension
        locHolder.populationTextView.text = item.population.toString()
        locHolder.typeTextView.text = item.type
    }

    class LocationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.item_location_list_name_tv
        val dimensionTextView: TextView = view.item_location_list_dimension_tv
        val populationTextView: TextView = view.item_location_list_population_tv
        val typeTextView: TextView = view.item_location_list_type_tv
    }
}