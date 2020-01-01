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
) : RecyclerView.Adapter<BoxesAdapter.BoxViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var boxes = emptyList<Box>()

    inner class BoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoxViewHolder {
        val itemView = inflater.inflate(R.layout.box_item, parent, false)
        return BoxViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BoxViewHolder, position: Int) {
        val current = boxes[position]
        holder.wordItemView.text =
            current.state.name + " " + current.content + " " + current.timestamp
    }

    override fun getItemId(position: Int): Long {
        return boxes[position].uid
    }

    internal fun setBoxes(boxes: List<Box>) {
        this.boxes = boxes
        notifyDataSetChanged()
    }

    override fun getItemCount() = boxes.size
}