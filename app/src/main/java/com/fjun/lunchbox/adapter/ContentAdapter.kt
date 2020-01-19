package com.fjun.lunchbox.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjun.lunchbox.R

/**
 * Adapter for Content in a lunch box. Support clicking on the rows via the onClick callback. Content need to be unique.
 */
class ContentAdapter internal constructor(
    context: Context,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contents = emptyList<String>()

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.text)

        fun bind(content: String) {
            text.text = content
            itemView.setOnClickListener { onClick(content) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ContentViewHolder(inflater.inflate(R.layout.box_item, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contentHolder = holder as ContentViewHolder
        val box = contents[position]
        contentHolder.bind(box)
    }

    override fun getItemId(position: Int) = contents[position].hashCode().toLong()

    internal fun setContents(contents: List<String>) {
        this.contents = contents
        notifyDataSetChanged()
    }

    override fun getItemCount() = contents.size
}