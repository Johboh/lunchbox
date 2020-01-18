package com.fjun.lunchbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjun.lunchbox.database.Box

class BoxesAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var boxes = emptyList<Box>()

    inner class BoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.text)

        fun bind(box: Box) {
            text.text = box.state.name + " " + box.content + " " + box.timestamp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BoxViewHolder(inflater.inflate(R.layout.box_item, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val boxHolder = holder as BoxViewHolder
        val box = boxes[position]
        boxHolder.bind(box)
    }

    override fun getItemId(position: Int) = boxes[position].uid

    internal fun setBoxes(boxes: List<Box>) {
        this.boxes = boxes
        notifyDataSetChanged()
    }

    override fun getItemCount() = boxes.size
}