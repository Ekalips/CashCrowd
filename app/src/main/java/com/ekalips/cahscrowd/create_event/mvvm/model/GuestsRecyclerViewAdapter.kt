package com.ekalips.cahscrowd.create_event.mvvm.model

import android.support.v7.util.DiffUtil
import android.util.TypedValue
import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.RvItemGuestBinding
import java.lang.ref.WeakReference

class GuestsRecyclerViewAdapter(callback: AdapterCallbacks? = null) : BindingRecyclerViewAdapter<RvItemGuestBinding, GuestUserWrap>(DIFF_CALLBACK) {
    override val resId: Int = R.layout.rv_item_guest

    val callback = WeakReference(callback)
    private var expandedStates = BooleanArray(0)

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemGuestBinding>, position: Int) {
        val item = getItem(holder.adapterPosition)
        holder.binding.user = item
        holder.binding.onContactClick = Runnable { changeExpandState(holder, !holder.binding.expanded, true) }
        holder.binding.onRemoveClick = Runnable { callback.get()?.onRemoveClick(item) }
        changeExpandState(holder, false, false)
    }

    private fun changeExpandState(holder: BindingViewHolder<RvItemGuestBinding>, expaned: Boolean, animate: Boolean) {
        expandedStates[holder.adapterPosition] = expaned
        holder.binding.expanded = expaned
        holder.binding.executePendingBindings()

        val newTranslation = if (expaned) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -68f, holder.itemView.context.resources.displayMetrics) else 0f
        holder.binding.contentHolder.animate()
                .translationX(newTranslation)
                .setDuration(if (animate) ANIMATION_SPEED else 0)
                .start()
    }

    override fun onViewRecycled(holder: BindingViewHolder<RvItemGuestBinding>) {
        super.onViewRecycled(holder)
        holder.binding.contentHolder.animation?.cancel()
    }

    override fun onViewDetachedFromWindow(holder: BindingViewHolder<RvItemGuestBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.binding.contentHolder.animation?.cancel()
    }

    override fun submitList(list: List<GuestUserWrap>?) {
        super.submitList(list)
        expandedStates = BooleanArray(list?.size ?: 0)
    }

    companion object {
        private const val ANIMATION_SPEED = 300L
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GuestUserWrap>() {
            override fun areItemsTheSame(oldItem: GuestUserWrap?, newItem: GuestUserWrap?): Boolean = oldItem?.id == newItem?.id
            override fun areContentsTheSame(oldItem: GuestUserWrap?, newItem: GuestUserWrap?): Boolean = oldItem == newItem
        }

        interface AdapterCallbacks {
            fun onRemoveClick(guest: GuestUserWrap)
        }
    }
}