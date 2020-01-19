package com.fjun.lunchbox.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjun.lunchbox.R
import com.fjun.lunchbox.database.Box
import com.fjun.lunchbox.database.State
import java.text.SimpleDateFormat
import java.util.*

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
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = box.timestamp
            val since =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(calendar.time)

            text.text = when (box.state) {
                State.FREEZER, State.FRIDGE -> text.context.getString(
                    R.string.box_title_with_content,
                    box.name,
                    box.content,
                    since
                )
                else -> text.context.getString(R.string.box_title_without_content, box.name)
            }
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