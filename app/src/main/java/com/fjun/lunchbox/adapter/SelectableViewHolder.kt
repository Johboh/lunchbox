package com.fjun.lunchbox.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Define a RecyclerView.ViewHolder that can be selected / drag and dropped
 */
abstract class SelectableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun setSelected(selected: Boolean)
}