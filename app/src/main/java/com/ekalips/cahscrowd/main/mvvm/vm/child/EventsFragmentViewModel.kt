package com.ekalips.cahscrowd.main.mvvm.vm.child

import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.PagedList
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.paging.NetworkState
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import javax.inject.Inject

class EventsFragmentViewState : BaseViewState() {

    val events = MediatorLiveData<PagedList<Event>>()
    val error = MediatorLiveData<String>()
    val refreshing = MediatorLiveData<Boolean>()

    val addEventTrigger = SingleLiveEvent<Void>()

}

class EventsFragmentViewModel @Inject constructor(eventsDataProvider: EventsDataProvider) : CCViewModel<EventsFragmentViewState>() {
    override val state: EventsFragmentViewState = EventsFragmentViewState()
    private val listing = eventsDataProvider.getEvents()

    init {
        state.events.addSource(listing.pagedList, { state.events.postValue(it) })
        state.loading.addSource(listing.networkState, { state.loading.postValue(it == NetworkState.LOADING) })
        state.refreshing.addSource(listing.refreshState, { state.refreshing.postValue(it == NetworkState.LOADING) })
    }

    fun refresh() {
        listing.refresh()
    }

    fun onAddClick(){
        state.addEventTrigger.call()
    }
}