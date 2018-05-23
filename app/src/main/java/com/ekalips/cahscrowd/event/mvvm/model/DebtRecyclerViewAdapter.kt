package com.ekalips.cahscrowd.event.mvvm.model

import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.statistics.DebtData
import com.ekalips.cahscrowd.databinding.RvItemDebtDataBinding

class DebtRecyclerViewAdapter : BindingRecyclerViewAdapter<RvItemDebtDataBinding, DebtData>() {
    override val resId: Int = R.layout.rv_item_debt_data

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemDebtDataBinding>, position: Int) {
        holder.binding.item = getItem(holder.adapterPosition)
    }
}