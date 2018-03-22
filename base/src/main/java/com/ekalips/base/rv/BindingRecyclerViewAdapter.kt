package com.ekalips.base.rv

import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup

abstract class BindingRecyclerViewAdapter<ViewBinding : ViewDataBinding, DataType>(comparator: DiffUtil.ItemCallback<DataType>)
    : ListAdapter<DataType, BindingViewHolder<ViewBinding>>(comparator) {

    constructor() : this(DefaultDiffComparator())

    abstract val resId: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewBinding> = BindingViewHolder(resId, parent)
}

class DefaultDiffComparator<DataType> : DiffUtil.ItemCallback<DataType>() {
    override fun areItemsTheSame(oldItem: DataType, newItem: DataType): Boolean = false
    override fun areContentsTheSame(oldItem: DataType, newItem: DataType): Boolean = false
}