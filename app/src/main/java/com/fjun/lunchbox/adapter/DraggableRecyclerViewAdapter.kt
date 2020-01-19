package com.fjun.lunchbox.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * Interface for an Adapter that have items that can be dragged.
 */
interface DraggableRecyclerViewAdapter {
    fun onRowMoved(fromPosition: Int, toPosition: Int)
    fun onItemDropped(fromPosition: Int, toPosition: Int)
    fun onRowSelected(myViewHolder: RecyclerView.ViewHolder)
    fun onRowClear(myViewHolder: RecyclerView.ViewHolder)
}