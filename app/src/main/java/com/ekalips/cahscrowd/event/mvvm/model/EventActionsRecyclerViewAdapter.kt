package com.ekalips.cahscrowd.event.mvvm.model

import android.support.v7.util.DiffUtil
import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.databinding.RvItemEventActionBinding

class EventActionsRecyclerViewAdapter : BindingRecyclerViewAdapter<RvItemEventActionBinding, Action>(DIFF_CALLBACK) {
    override val resId: Int = R.layout.rv_item_event_action

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemEventActionBinding>, position: Int) {
        holder.binding.action = getItem(holder.adapterPosition)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Action>() {
            override fun areItemsTheSame(oldItem: Action?, newItem: Action?): Boolean = oldItem?.id == newItem?.id
            override fun areContentsTheSame(oldItem: Action?, newItem: Action?): Boolean = Action.compare(oldItem, newItem)
        }
    }
}