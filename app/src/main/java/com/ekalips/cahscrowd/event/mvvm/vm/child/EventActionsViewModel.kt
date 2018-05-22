package com.ekalips.cahscrowd.event.mvvm.vm.child

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.ActionsDataProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.data.Listing
import com.ekalips.cahscrowd.stuff.paging.NetworkState
import com.ekalips.cahscrowd.stuff.paging.Status
import javax.inject.Inject

class EventActionsViewState : BaseViewState() {
    val eventId = MutableLiveData<String>()
    val actions = MediatorLiveData<List<Action>>()
    val error = MediatorLiveData<String>()
}

class EventActionsViewModel @Inject constructor(private val actionsDataProvider: ActionsDataProvider) : CCViewModel<EventActionsViewState>() {
    override val state = EventActionsViewState()

    init {
        state.actions.addSource(state.eventId, {
            it?.let {
                initListing(actionsDataProvider.getActionsForEventLiveData(it))
            }
        })
    }

    private fun initListing(listing: Listing<Action>) {
        state.actions.addSource(listing.data, { state.actions.postValue(it) })
        state.loading.addSource(listing.networkState, { state.loading.postValue(it == NetworkState.LOADING) })
        state.error.addSource(listing.networkState, {
            if (it?.status == Status.FAILED) {
                state.error.postValue(it.msg)
            }
        })
    }

    fun setEventId(eventId: String) {
        state.eventId.postValue(eventId)
    }
}