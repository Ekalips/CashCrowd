package com.ekalips.cahscrowd.event.mvvm.model

import android.view.ViewGroup
import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.base.stuff.views.CircleOutlineProvider
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.databinding.RvItemEventActionBinding

class EventActionsRecyclerViewAdapter : BindingRecyclerViewAdapter<RvItemEventActionBinding, Action>() {
    override val resId: Int = R.layout.rv_item_event_action

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<RvItemEventActionBinding> {
        return super.onCreateViewHolder(parent, viewType).also {
            it.binding.userIv.outlineProvider = CircleOutlineProvider()
            it.binding.userIv.clipToOutline = true
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemEventActionBinding>, position: Int) {
        holder.binding.action = getItem(holder.adapterPosition)
    }
}