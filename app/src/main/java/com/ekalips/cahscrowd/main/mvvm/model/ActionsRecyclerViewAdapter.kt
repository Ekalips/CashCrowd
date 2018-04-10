package com.ekalips.cahscrowd.main.mvvm.model

import android.databinding.ViewDataBinding
import android.view.ViewGroup
import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.base.stuff.views.CircleOutlineProvider
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.databinding.RvItemEventActionBinding

class ActionsRecyclerViewAdapter : BindingRecyclerViewAdapter<ViewDataBinding, Action>() {
    override val resId: Int = 0

    private var useLarge = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> {
        return BindingViewHolder<RvItemEventActionBinding>(R.layout.rv_item_event_action, parent).also {
            it.binding.userIv.outlineProvider = CircleOutlineProvider()
            it.binding.userIv.clipToOutline = true
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDataBinding>, position: Int) {
        holder.binding.setVariable(BR.action, getItem(holder.adapterPosition))
        holder.binding.setVariable(BR.expanded, useLarge)
        holder.binding.executePendingBindings()
    }

    fun changeMode(useLarge: Boolean) {
        if (this.useLarge != useLarge) {
            this.useLarge = useLarge
        }
    }
}