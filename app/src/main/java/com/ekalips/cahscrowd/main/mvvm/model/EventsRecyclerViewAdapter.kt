package com.ekalips.cahscrowd.main.mvvm.model

import android.graphics.drawable.Animatable
import android.support.transition.AutoTransition
import android.support.transition.Transition
import android.support.transition.TransitionManager
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.databinding.RvItemEventBinding
import com.ekalips.cahscrowd.stuff.TransitionListenerAdapter
import com.ekalips.cahscrowd.stuff.utils.disposeBy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.SoftReference
import java.util.concurrent.atomic.AtomicBoolean

class EventsRecyclerViewAdapter(callback: AdapterCallbacks? = null) : BindingRecyclerViewAdapter<RvItemEventBinding, Event>(DIFF_CALLBACK) {
    override val resId: Int = R.layout.rv_item_event

    private val disposables = HashMap<Any, CompositeDisposable?>()
    private val expandedStates = HashMap<String, Boolean>()
    private var expandLock = AtomicBoolean()
    private val callback = SoftReference(callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<RvItemEventBinding> {
        return super.onCreateViewHolder(parent, viewType).also {
            it.binding.actionsRv.adapter = ActionsRecyclerViewAdapter()
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemEventBinding>, position: Int) {
        val item = getItem(holder.adapterPosition)
        clearAsyncFor(holder.binding.root)
        collapseOrExpand(holder, !(expandedStates[item?.id] ?: false), false)
        holder.binding.event = item
        (holder.binding.actionsRv.adapter as ActionsRecyclerViewAdapter).submitList(item?.actions)
        holder.binding.onEventClick = Runnable { item?.let { callback.get()?.onEventClicked(item) } }
        holder.binding.expandIv.setOnClickListener {
            if (!expandLock.get()) {
                collapseOrExpand(holder)
                expandedStates[item?.id ?: ""] = !(expandedStates[item?.id] ?: true)
            }
        }
        calcStuffFor(holder.binding.root, item, { holder.binding.newCount = it })
        holder.binding.executePendingBindings()
    }

    private fun collapseOrExpand(holder: BindingViewHolder<RvItemEventBinding>) {
        val manager = holder.binding.actionsRv.layoutManager as LinearLayoutManager
        val expanded = (manager.orientation == LinearLayoutManager.VERTICAL)
        collapseOrExpand(holder, expanded, true)
    }

    private fun collapseOrExpand(holder: BindingViewHolder<RvItemEventBinding>, collapse: Boolean, animate: Boolean) {
        if (animate) {
            val transition = AutoTransition().addListener(getTransitionAdapter())
            TransitionManager.beginDelayedTransition(holder.binding.root.parent as ViewGroup, transition)
        }
        val manager = holder.binding.actionsRv.layoutManager as LinearLayoutManager
        manager.orientation = if (collapse) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL
        holder.binding.lastActionTimeTv.visibility = if (collapse) View.VISIBLE else View.GONE
        holder.binding.actionsCounterContainer.visibility = if (collapse) View.VISIBLE else View.GONE
        (holder.binding.actionsRv.adapter as ActionsRecyclerViewAdapter).changeMode(!collapse)
        changeArrow(holder, collapse, animate)
    }

    private fun changeArrow(holder: BindingViewHolder<RvItemEventBinding>, collapse: Boolean, animate: Boolean) {
        if (animate) {
            holder.binding.expandIv.setImageResource(if (collapse) R.drawable.ic_arrow_up_down else R.drawable.ic_arrow_down_up)
            if (holder.binding.expandIv.drawable is Animatable) {
                (holder.binding.expandIv.drawable as Animatable).start()
            }
        } else {
            holder.binding.expandIv.setImageResource(if (collapse) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up)
        }
    }

    private fun getTransitionAdapter() = object : TransitionListenerAdapter() {
        override fun onTransitionStart(transition: Transition) {
            expandLock.set(true)
        }

        override fun onTransitionEnd(transition: Transition) {
            expandLock.set(false)
        }
    }

    private fun clearAsyncFor(key: Any) {
        disposables[key]?.clear()
        disposables[key] = null
    }

    private fun calcStuffFor(key: Any, item: Event?, onNewCountReady: ((Int) -> Unit)) {
        val disposer = CompositeDisposable()
        Observable.fromIterable(item?.actions.orEmpty()).filter { it.newAction }.count().subscribe({ onNewCountReady(it.toInt()) }, { it.printStackTrace() }).disposeBy(disposer)
        disposables[key] = disposer
    }

    override fun submitList(data: List<Event>?) {
        expandedStates.clear()
        data?.forEach {
            expandedStates[it.id] = false
        }
        super.submitList(data)
    }


    interface AdapterCallbacks {
        fun onEventClicked(event: Event)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event?, newItem: Event?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Event?, newItem: Event?): Boolean {
                return oldItem?.name == newItem?.name && oldItem?.description == newItem?.description // todo check list
            }

        }
    }
}