package com.ekalips.cahscrowd.event.mvvm.model

import android.support.v7.util.DiffUtil
import com.ekalips.base.rv.BindingRecyclerViewAdapter
import com.ekalips.base.rv.BindingViewHolder
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.databinding.RvItemEventParticipantBinding

class EventParticipantsRecyclerViewAdapter : BindingRecyclerViewAdapter<RvItemEventParticipantBinding, BaseUser>(DIFF_CALLBACK) {
    override val resId: Int = R.layout.rv_item_event_participant

    override fun onBindViewHolder(holder: BindingViewHolder<RvItemEventParticipantBinding>, position: Int) {
        holder.binding.user = getItem(holder.adapterPosition)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BaseUser>() {
            override fun areItemsTheSame(oldItem: BaseUser?, newItem: BaseUser?): Boolean = oldItem?.id == newItem?.id
            override fun areContentsTheSame(oldItem: BaseUser?, newItem: BaseUser?): Boolean {
                return oldItem?.name == newItem?.name &&
                        oldItem?.avatar == newItem?.avatar &&
                        oldItem?.loaded == newItem?.loaded
            }
        }
    }
}