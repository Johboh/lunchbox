package com.fjun.lunchbox

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.String.format

/**
 * An adapter with adapters. Support showing and hiding adapters.
 */
class SectionedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val adapterHolders = linkedMapOf<Short, AdapterHolder>()

    fun addAdapter(id: Short, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
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
        holderWithOffset.holder.adapter.onBindViewHolder(holder, position - holderWithOffset.offset)
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
        val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        val id: Short
    ) {
        var visible: Boolean = true
    }

    data class AdapterHolderWithOffset(val holder: AdapterHolder, val offset: Int)
}