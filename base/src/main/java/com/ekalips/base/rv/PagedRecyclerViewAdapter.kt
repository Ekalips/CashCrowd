package com.ekalips.base.rv

import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup

abstract class PagedRecyclerViewAdapter<ViewBinding : ViewDataBinding, DataType>(comparator: DiffUtil.ItemCallback<DataType>) :
        ListAdapter<DataType, BindingViewHolder<ViewBinding>>(comparator) {

    abstract val resId: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewBinding> = BindingViewHolder(resId, parent)

}