package com.ekalips.cahscrowd.main.mvvm.model

import android.support.v7.util.DiffUtil
import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.databinding.RvItemEventActionCollapsibleBinding

class ActionsRecyclerViewAdapter : BindingRecyclerViewAdapter<RvItemEventActionCollapsibleBinding, Action>(DIFF_COMPARATOR) {
    override val resId: Int = R.layout.rv_item_event_action_collapsible

    private var useLarge = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemEventActionCollapsibleBinding>, position: Int) {
        holder.binding.setVariable(BR.action, getItem(holder.adapterPosition))
        holder.binding.setVariable(BR.expanded, useLarge)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (!useLarge) Math.min(super.getItemCount(), MAX_COUNT_IN_COLLAPSED) else super.getItemCount()
    }

    fun changeMode(useLarge: Boolean) {
        if (this.useLarge != useLarge) {
            this.useLarge = useLarge
        }
    }

    companion object {
        val MAX_COUNT_IN_COLLAPSED = 5
        val DIFF_COMPARATOR = object : DiffUtil.ItemCallback<Action>() {
            override fun areItemsTheSame(oldItem: Action?, newItem: Action?): Boolean = oldItem?.id == newItem?.id
            override fun areContentsTheSame(oldItem: Action?, newItem: Action?): Boolean = false
        }
    }
}