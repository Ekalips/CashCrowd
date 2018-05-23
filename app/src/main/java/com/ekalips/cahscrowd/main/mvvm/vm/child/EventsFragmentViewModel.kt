package com.ekalips.cahscrowd.main.mvvm.vm.child

import android.arch.lifecycle.MediatorLiveData
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.providers.ResourceProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.paging.NetworkState
import com.ekalips.cahscrowd.stuff.paging.Status
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import javax.inject.Inject

class EventsFragmentViewState : BaseViewState() {

    val events = MediatorLiveData<List<Event>>()
    val error = MediatorLiveData<String>()

    val addEventTrigger = SingleLiveEvent<Void>()
    val updateTrigger = SingleLiveEvent<Void>()

}

class EventsFragmentViewModel @Inject constructor(private val eventsDataProvider: EventsDataProvider,
                                                  private val resourceProvider: ResourceProvider) : CCViewModel<EventsFragmentViewState>() {
    override val state: EventsFragmentViewState = EventsFragmentViewState()

    private val listing = eventsDataProvider.getEventsListing()

    init {
        state.events.addSource(listing.data, { state.events.postValue(it) })
        state.loading.addSource(listing.networkState, { state.loading.postValue(it == NetworkState.LOADING) })
        state.error.addSource(listing.networkState, {
            if (it?.status == Status.FAILED) {
                state.error.postValue(it.msg)
            }
        })

        listing.refresh()
    }

    fun refresh() {
        listing.refresh()
    }

    fun onAddClick() {
        state.addEventTrigger.call()
    }

    fun acceptInviteCode(value: String?): Boolean {
        return if (value.isNullOrBlank()) {
            false
        } else {
            acceptInvite(value!!)
            true
        }
    }

    private fun acceptInvite(code: String) {
        eventsDataProvider.acceptInvite(code)
                .subscribe({ state.toast.postValue(resourceProvider.getString(R.string.success_accept_invite)) },
                        { state.toast.postValue(resourceProvider.getString(R.string.error_accept_invite)) })
    }
}