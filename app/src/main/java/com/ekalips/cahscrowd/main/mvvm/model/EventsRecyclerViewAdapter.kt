package com.ekalips.cahscrowd.main.mvvm.model

import android.support.v7.util.DiffUtil
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.base.rv.PagedRecyclerViewAdapter
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.databinding.RvItemEventBinding
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class EventsRecyclerViewAdapter : PagedRecyclerViewAdapter<RvItemEventBinding, Event>(comparator) {
    override val resId: Int = R.layout.rv_item_event

    private val disposablesMap = HashMap<Any, CompositeDisposable>()

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemEventBinding>, position: Int) {
        disposablesMap[holder.binding.root]?.dispose()

        val item = getItem(holder.adapterPosition)
        holder.binding.event = item

        if (item != null) {
            val disposables = CompositeDisposable()
            disposables.add(Observable.fromIterable(item.actions ?: ArrayList())
                    .filter { !it.seen }.count()
                    .subscribe({ holder.binding.newCount = it.toInt() }, { it.printStackTrace() }))

            disposablesMap[holder.binding.root] = disposables
        }
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