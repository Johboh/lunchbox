package com.fjun.lunchbox

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.String.format

/**
 * An adapter with adapters. Support showing and hiding adapters.
 */
class SectionedAdapter(private val dropCallback: (fromPosition: Int, fromId: Short, toId: Short) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    DraggableRecyclerViewAdapter {

    init {
        setHasStableIds(true)
    }

    private val adapterHolders = linkedMapOf<Short, AdapterHolder>()

    fun addAdapter(
        id: Short,
        adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>
    ) {
        if (id in adapterHolders) {
            throw Exception(format("The ID %d already exist", id))
        }
        adapterHolders[id] = AdapterHolder(adapter, id)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                notifyDataSetChanged()
            }
        })
    }

    fun showAdapter(id: Short, show: Boolean) {
        if (id !in adapterHolders) {
            throw Exception(format("The ID %d does not exist", id))
        }
        adapterHolders[id]?.visible = show
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val originalViewType = viewType shr 16
        val id: Short = (viewType and 0xFFFF).toShort()
        val holder = adapterHolders[id] ?: throw Exception(format("The ID %d does not exist", id))
        return holder.adapter.onCreateViewHolder(parent, originalViewType)
    }

    override fun getItemViewType(position: Int): Int {
        val holderWithOffset = holderForPosition(position)
        val holder = holderWithOffset.holder
        val viewType = holder.adapter.getItemViewType(position - holderWithOffset.offset)
        val modifiedViewType = viewType shl 16
        return modifiedViewType + holder.id
    }

    override fun getItemId(position: Int): Long {
        val holderWithOffset = holderForPosition(position)
        val holder = holderWithOffset.holder
        return holder.adapter.getItemId(position - holderWithOffset.offset) + holder.adapter.hashCode()
    }

    override fun getItemCount(): Int {
        var count = 0
        for (holder in adapterHolders.values) {
            count += if (holder.visible) holder.adapter.itemCount else 0
        }
        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderWithOffset = holderForPosition(position)
        val adapter =
            holderWithOffset.holder.adapter as RecyclerView.Adapter<RecyclerView.ViewHolder?>
        adapter.onBindViewHolder(holder, position - holderWithOffset.offset)
    }

    private fun holderForPosition(position: Int): AdapterHolderWithOffset {
        var count = 0
        for (holder in adapterHolders.values) {
            if (!holder.visible) {
                continue
            }
            count += holder.adapter.itemCount
            if (position < count) {
                return AdapterHolderWithOffset(holder, count - holder.adapter.itemCount)
            }
        }
        throw Exception(format("Position %d not in list", position))
    }

    data class AdapterHolder(
        val adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
        val id: Short
    ) {
        var visible: Boolean = true
    }

    data class AdapterHolderWithOffset(val holder: AdapterHolder, val offset: Int)

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
    }

    override fun onItemDropped(fromPosition: Int, toPosition: Int) {
        val fromHolder = holderForPosition(fromPosition)
        dropCallback(
            fromPosition - fromHolder.offset,
            fromHolder.holder.id,
            holderForPosition(toPosition).holder.id
        )
    }

    override fun onRowSelected(myViewHolder: RecyclerView.ViewHolder) {
        if (myViewHolder is SelectableViewHolder) {
            myViewHolder.setSelected(true)
        }
    }

    override fun onRowClear(myViewHolder: RecyclerView.ViewHolder) {
        if (myViewHolder is SelectableViewHolder) {
            myViewHolder.setSelected(false)
        }
    }
}