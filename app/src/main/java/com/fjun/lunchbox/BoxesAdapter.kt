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

    private val typeHeader = 0;
    private val typeBox = 1;

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var freezerBoxes = emptyList<Box>()
    private var elseBoxes = emptyList<Box>()

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.text)
    }

    inner class BoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)

        fun bind(box: Box) {
            text.text = box.state.name + " " + box.content + " " + box.timestamp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            typeHeader -> HeaderViewHolder(inflater.inflate(R.layout.box_header, parent, false))
            typeBox -> BoxViewHolder(inflater.inflate(R.layout.box_item, parent, false))
            else -> throw RuntimeException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && freezerBoxes.isNotEmpty()) {
            val headerHolder = holder as HeaderViewHolder
            headerHolder.title.setText(R.string.box_section_title_freezer)
        } else if (position - 1 < freezerBoxes.size) {
            val boxHolder = holder as BoxViewHolder
            boxHolder.bind(freezerBoxes[position - 1])
        } else if (position - 1 == freezerBoxes.size && elseBoxes.isNotEmpty()) {
            val headerHolder = holder as HeaderViewHolder
            headerHolder.title.setText(R.string.box_section_title_else)
        } else {
            val boxHolder = holder as BoxViewHolder
            val box = elseBoxes[position - freezerBoxes.size - 2]
            boxHolder.bind(box)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return typeHeader;
        }
        if (position - 1 < freezerBoxes.size) {
            return typeBox;
        }
        if (position - 1 == freezerBoxes.size && elseBoxes.isNotEmpty()) {
            return typeHeader;
        }
        return typeBox;
    }

    internal fun setFreezerBoxes(boxes: List<Box>) {
        this.freezerBoxes = boxes
        notifyDataSetChanged()
    }

    internal fun setElseBoxes(boxes: List<Box>) {
        this.elseBoxes = boxes
        notifyDataSetChanged()
    }

    override fun getItemCount() =
        freezerBoxes.size + (if (freezerBoxes.isNotEmpty()) 1 else 0) + elseBoxes.size + (if (elseBoxes.isNotEmpty()) 1 else 0)
}