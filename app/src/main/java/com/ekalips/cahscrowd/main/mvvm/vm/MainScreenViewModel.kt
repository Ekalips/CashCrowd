package com.ekalips.cahscrowd.main.mvvm.vm

import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.PagedList
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.paging.NetworkState
import javax.inject.Inject

class MainScreenViewState : BaseViewState() {

    val events = MediatorLiveData<PagedList<Event>>()
    val error = MediatorLiveData<String>()
}

class MainScreenViewModel @Inject constructor(eventsDataProvider: EventsDataProvider) : CCViewModel<MainScreenViewState>() {
    override val state: MainScreenViewState = MainScreenViewState()
    private val listing = eventsDataProvider.getEvents()

    init {
        state.events.addSource(listing.pagedList, { state.events.postValue(it) })
        state.loading.addSource(listing.networkState, { state.loading.postValue(it != NetworkState.LOADED) })
    }
}