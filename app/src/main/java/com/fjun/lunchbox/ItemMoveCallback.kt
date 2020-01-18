package com.fjun.lunchbox

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemMoveCallback(private val adapter: DraggableRecyclerViewAdapter) :
    ItemTouchHelper.Callback() {

    private var dragToPosition: Int = 0
    private var dragFromPosition: Int = 0

    override fun isLongPressDragEnabled() = true
    override fun isItemViewSwipeEnabled() = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) = if (viewHolder is SelectableViewHolder) makeMovementFlags(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        0
    ) else makeMovementFlags(0, 0)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        dragToPosition = target.adapterPosition
        adapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                viewHolder?.also { dragFromPosition = it.adapterPosition }
            }
            ItemTouchHelper.ACTION_STATE_IDLE -> {
                if (dragFromPosition != -1 && dragToPosition != -1 && dragFromPosition != dragToPosition) {
                    // Item successfully dragged
                    adapter.onItemDropped(dragFromPosition, dragToPosition)
                    // Reset drag positions
                    dragFromPosition = -1
                    dragToPosition = -1
                }
            }
        }

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is RecyclerView.ViewHolder) {
                val myViewHolder: RecyclerView.ViewHolder = viewHolder
                adapter.onRowSelected(myViewHolder)
            }
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        adapter.onRowClear(viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}