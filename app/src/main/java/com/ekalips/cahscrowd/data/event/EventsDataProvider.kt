package com.ekalips.cahscrowd.data.event

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import com.ekalips.cahscrowd.data.event.local.LocalEventsDataStore
import com.ekalips.cahscrowd.data.event.paginate.EventsBoundaryCallback
import com.ekalips.cahscrowd.data.event.remote.RemoteEventDataStore
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.stuff.ErrorHandler
import com.ekalips.cahscrowd.stuff.paging.Listing
import com.ekalips.cahscrowd.stuff.paging.NetworkState
import com.ekalips.cahscrowd.stuff.utils.wrap
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsDataProvider @Inject constructor(private val userDataProvider: UserDataProvider,
                                             private val localEventsDataStore: LocalEventsDataStore,
                                             private val remoteEventDataStore: RemoteEventDataStore,
                                             private val errorHandler: ErrorHandler) {

    companion object {
        private const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }

    fun getEvents(): Listing<Event> {
        val boundaryCallback = EventsBoundaryCallback({ lastId ->
            userDataProvider.getAccessToken().flatMap { remoteEventDataStore.getEvents(it, lastId, DEFAULT_NETWORK_PAGE_SIZE) }.toObservable().wrap(errorHandler.getHandler())
        }, {
            saveEvents(it, false)
        })
        val factory = localEventsDataStore.getEventsDataSourceFactory()
        val builder = LivePagedListBuilder(factory, DEFAULT_NETWORK_PAGE_SIZE).setBoundaryCallback(boundaryCallback)
        val refreshTrigger = SingleLiveEvent<Void>()
        val refreshState = Transformations.switchMap(refreshTrigger, { refresh() })
        return Listing(pagedList = builder.build(),
                networkState = boundaryCallback.networkState,
                retry = {
                    boundaryCallback.helper.retryAllFailed()
                },
                refresh = {
                    refreshTrigger.call()
                },
                refreshState = refreshState)
    }

    private fun refresh(): LiveData<NetworkState> {
        val state = MutableLiveData<NetworkState>()
        state.value = NetworkState.LOADING
        userDataProvider.getAccessToken().flatMap { remoteEventDataStore.getEvents(it, null, DEFAULT_NETWORK_PAGE_SIZE) }
                .wrap(errorHandler.getHandler())
                .subscribe({
                    saveEvents(it, true)
                    state.postValue(NetworkState.LOADED)
                }, { state.postValue(NetworkState.error(it.message)) })
        return state
    }

    private fun saveEvents(events: List<Event>?, clear: Boolean = false) {
        localEventsDataStore.saveEvents(events, clear)
    }

}