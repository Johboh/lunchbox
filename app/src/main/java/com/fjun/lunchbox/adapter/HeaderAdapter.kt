package com.fjun.lunchbox.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjun.lunchbox.R

/**
 * Adapter for a single text header.
 */
class HeaderAdapter internal constructor(
    context: Context, private val title: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HeaderViewHolder(inflater.inflate(R.layout.box_header, parent, false))
    }

    override fun getItemId(position: Int): Long = (title.hashCode() + hashCode()).toLong()

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val headerHolder = holder as HeaderViewHolder
        headerHolder.title.text = this.title
    }
}