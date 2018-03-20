package com.ekalips.cahscrowd.main.mvvm.vm

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.PagedList
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.paging.NetworkState
import javax.inject.Inject

class MainScreenViewState(val events: LiveData<PagedList<Event>>) : BaseViewState() {

    val error = MediatorLiveData<String>()
    val refreshing = MediatorLiveData<Boolean>()
}

class MainScreenViewModel @Inject constructor(eventsDataProvider: EventsDataProvider) : CCViewModel<MainScreenViewState>() {
    override val state: MainScreenViewState
    private val listing = eventsDataProvider.getEvents()

    init {
        state = MainScreenViewState(listing.pagedList as LiveData<PagedList<Event>>)

        state.loading.addSource(listing.networkState, { state.loading.postValue(it == NetworkState.LOADING) })
        state.refreshing.addSource(listing.refreshState, { state.refreshing.postValue(it == NetworkState.LOADING) })
    }

    fun refresh() {
        listing.refresh()
    }
}