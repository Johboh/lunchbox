package com.fjun.lunchbox.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class SelectableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun setSelected(selected: Boolean)
}