package com.ekalips.cahscrowd.event.mvvm.model

import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.databinding.RvItemEventActionBinding

class EventActionsRecyclerView : BindingRecyclerViewAdapter<RvItemEventActionBinding, Action>(){
    override val resId: Int = R.layout.rv_item_event_action

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemEventActionBinding>, position: Int) {
        holder.binding.action = getItem(holder.adapterPosition)
    }
}