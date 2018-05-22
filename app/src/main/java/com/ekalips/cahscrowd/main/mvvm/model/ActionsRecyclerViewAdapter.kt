package com.ekalips.cahscrowd.main.mvvm.model

import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.data.action.Action

class ActionsRecyclerViewAdapter : BindingRecyclerViewAdapter<ViewDataBinding, Action>(DIFF_COMPARATOR) {
    override val resId: Int = 0

    private var useLarge = false
        set(value) {
            field = value
            notifyDataSetChanged()
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

    companion object {
        val DIFF_COMPARATOR = object : DiffUtil.ItemCallback<Action>() {
            override fun areItemsTheSame(oldItem: Action?, newItem: Action?): Boolean = oldItem?.id == newItem?.id
            override fun areContentsTheSame(oldItem: Action?, newItem: Action?): Boolean = Action.compare(oldItem, newItem)
        }
    }
}