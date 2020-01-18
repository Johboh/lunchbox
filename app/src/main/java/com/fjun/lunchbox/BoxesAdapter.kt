package com.fjun.lunchbox

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjun.lunchbox.database.Box

class BoxesAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<SelectableViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var boxes = emptyList<Box>()

    inner class BoxViewHolder(itemView: View) : SelectableViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.text)
        private val originalBackground = itemView.background;

        fun bind(box: Box) {
            text.text = box.name + " " + box.state.name + " " + box.content + " " + box.timestamp
        }

        override fun setSelected(selected: Boolean) {
            if (selected) {
                itemView.setBackgroundColor(Color.LTGRAY)
            } else {
                itemView.setBackgroundDrawable(originalBackground)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BoxViewHolder(inflater.inflate(R.layout.box_item, parent, false))

    override fun onBindViewHolder(holder: SelectableViewHolder, position: Int) {
        val boxHolder = holder as BoxViewHolder
        val box = boxes[position]
        boxHolder.bind(box)
    }

    override fun getItemId(position: Int) = boxes[position].uid + hashCode()

    internal fun setBoxes(boxes: List<Box>) {
        this.boxes = boxes
        notifyDataSetChanged()
    }

    override fun getItemCount() = boxes.size

    fun getBox(position: Int) = boxes.get(position)
}