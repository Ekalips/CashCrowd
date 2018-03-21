package com.ekalips.cahscrowd.main.mvvm.model

import android.support.v7.util.DiffUtil
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.base.rv.PagedRecyclerViewAdapter
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.databinding.RvItemEventBinding

class EventsRecyclerViewAdapter : PagedRecyclerViewAdapter<RvItemEventBinding, Event>(comparator) {
    override val resId: Int = R.layout.rv_item_event

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemEventBinding>, position: Int) {
        holder.binding.event = getItem(holder.adapterPosition)
    }

    companion object {
        private val comparator = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event?, newItem: Event?): Boolean =
                    oldItem?.id == newItem?.id

            override fun areContentsTheSame(oldItem: Event?, newItem: Event?): Boolean {
                return false
            }

        }
    }
}