package org.bagolysz.rickmortywiki.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_load_more.view.*
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.ui.data.BaseUiItem

abstract class BaseAdapter<Data : BaseUiItem>(context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var clickListener: ItemClickListener? = null
    private val dataList: MutableList<Data> = mutableListOf()
    protected val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    abstract fun createDataViewHolderItem(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun bindDataViewHolderItem(holder: RecyclerView.ViewHolder, item: Data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_ITEM) {
            createDataViewHolderItem(parent)
        } else {
            val view = layoutInflater.inflate(R.layout.list_item_load_more, parent, false)
            FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == DATA_ITEM) {
            val item = dataList[position]
            bindDataViewHolderItem(holder, item)
            holder.itemView.setOnClickListener { clickListener?.onItemClick(item.id) }
        } else if (holder.itemViewType == FOOTER_ITEM) {
            val footerHolder = holder as FooterViewHolder
            if (position != 0) {
                footerHolder.view.visibility = View.VISIBLE
                footerHolder.loadMoreButton.setOnClickListener {
                    clickListener?.onLoadMoreItemsClick()
                }
            } else {
                footerHolder.view.visibility = View.GONE
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return if (position == dataList.size)
            0
        else
            dataList[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return dataList.size + 1 // for load more button
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < dataList.size)
            DATA_ITEM
        else
            FOOTER_ITEM
    }

    fun setDataList(list: List<Data>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.clickListener = listener
    }

    class FooterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val loadMoreButton: Button = view.item_character_list_load_more_button
    }

    interface ItemClickListener {
        fun onItemClick(itemId: Int)

        fun onLoadMoreItemsClick()
    }

    companion object {
        private const val DATA_ITEM = 0
        private const val FOOTER_ITEM = 1
    }
}