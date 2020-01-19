package com.fjun.lunchbox.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjun.lunchbox.R
import com.fjun.lunchbox.database.Box
import com.fjun.lunchbox.database.State

/**
 * Adapter for lunch boxes.
 */
class BoxesAdapter internal constructor(
    context: Context,
    private val onOverflowClick: (Box) -> Unit
) : RecyclerView.Adapter<SelectableViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var boxes = emptyList<Box>()

    inner class BoxViewHolder(itemView: View) : SelectableViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.text)
        private val overflow: ImageView = itemView.findViewById(R.id.context_menu)
        private val originalBackground = itemView.background;

        fun bind(box: Box) {
            val days = ((System.currentTimeMillis() - box.timestamp) / 86400000).toInt()

            text.text = when (box.state) {
                State.FREEZER, State.FRIDGE -> text.context.resources.getQuantityString(
                    R.plurals.box_title_with_content,
                    days,
                    box.name,
                    box.content,
                    days
                )
                else -> text.context.getString(R.string.box_title_without_content, box.name)
            }
            overflow.setOnClickListener { _ -> onOverflowClick(box) }
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

    override fun getItemId(position: Int) = boxes[position].uid

    internal fun setBoxes(boxes: List<Box>) {
        this.boxes = boxes
        notifyDataSetChanged()
    }

    override fun getItemCount() = boxes.size

    fun getBox(position: Int) = boxes.get(position)
}