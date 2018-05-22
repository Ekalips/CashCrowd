package com.ekalips.cahscrowd.data.event

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import com.ekalips.cahscrowd.data.event.local.LocalEventsDataStore
import com.ekalips.cahscrowd.data.event.remote.RemoteEventDataStore
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.stuff.ErrorHandler
import com.ekalips.cahscrowd.stuff.data.Listing
import com.ekalips.cahscrowd.stuff.paging.NetworkState
import com.ekalips.cahscrowd.stuff.utils.wrap
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsDataProvider @Inject constructor(private val userDataProvider: UserDataProvider,
                                             private val localEventsDataStore: LocalEventsDataStore,
                                             private val remoteEventDataStore: RemoteEventDataStore,
                                             private val errorHandler: ErrorHandler) {

    companion object {
        private const val DEFAULT_NETWORK_PAGE_SIZE = 30
        private const val TAG = "EventsDataProvider"
    }

//    fun getEventsListing(): PagedListing<Event> {
//        val boundaryCallback = EventsBoundaryCallback({ lastId -> fetchEventsRemotely(lastId) },
//                { saveEvents(it, false) })
//        val factory = localEventsDataStore.getEventsDataSourceFactory() as DataSource.Factory<Int, Event>
//        val builder = LivePagedListBuilder(factory, DEFAULT_NETWORK_PAGE_SIZE)
//                .setBoundaryCallback(boundaryCallback)
//        val refreshTrigger = MutableLiveData<Void>()
//        val refreshState = Transformations.switchMap(refreshTrigger, { refresh() })
//        return PagedListing(pagedList = builder.build(),
//                networkState = boundaryCallback.networkState,
//                retry = {
//                    boundaryCallback.helper.retryAllFailed()
//                },
//                refresh = {
//                    refreshTrigger.value = null
//                },
//                refreshState = refreshState)
//    }

    fun getEvents(): Observable<List<Event>> {
        return Observable.concatDelayError(Observable.just(localEventsDataStore.getEvents(),
                userDataProvider.getAccessToken().flatMap { remoteEventDataStore.getEvents(it) }.toObservable()
                        .doOnNext({ saveEvents(it) })))
                .wrap(errorHandler.getHandler())
    }

    fun getEventsListing(): Listing<Event> {
        val data = localEventsDataStore.getEventsLiveData()
        val refreshTrigger = SingleLiveEvent<Void>()
        val networkState = Transformations.switchMap(refreshTrigger, { refresh() })
        return Listing(data = data, networkState = networkState, refresh = { refreshTrigger.call() }, retry = { refreshTrigger.call() })
    }

    fun getEventParticipants(eventId: String): Single<List<BaseUser>> {
        return userDataProvider.getAccessToken().flatMap { remoteEventDataStore.getEventParticipants(it, eventId) }
                .doAfterSuccess { userDataProvider.saveUsers(*it.toTypedArray()) }
                .wrap(errorHandler.getHandler())
    }

    fun createEvent(title: String, description: String): Completable {
        return userDataProvider.getAccessToken().flatMap { remoteEventDataStore.crateEvent(it, title, description) }
                .doOnSuccess { saveEvents(listOf(it), false) }
                .toCompletable()
                .wrap(errorHandler.getHandler())
    }

    fun getEvent(eventId: String): LiveData<Event> {
        userDataProvider.getAccessToken().flatMap { remoteEventDataStore.getEvent(it, eventId) }
                .wrap(errorHandler.getHandler())
                .subscribe({ saveEvents(listOf(it)) }, { Log.e(TAG, "Error loading event: $eventId", it) })
        return localEventsDataStore.getEvent(eventId)
    }

    fun getEventShareLink(eventId: String): Single<Pair<String, String>> {
        return userDataProvider.getAccessToken().flatMap { remoteEventDataStore.getEventShareLink(it, eventId) }
                .wrap(errorHandler.getHandler())
    }

    private fun refresh(): LiveData<NetworkState> {
        val state = MutableLiveData<NetworkState>()
        state.value = NetworkState.LOADING
        fetchEventsRemotely()
                .subscribe({
                    saveEvents(it, true)
                    state.postValue(NetworkState.LOADED)
                }, { state.postValue(NetworkState.error(it.message)) })
        return state
    }


    private fun fetchEventsRemotely(): Observable<List<Event>> {
        return userDataProvider.getAccessToken().flatMap { remoteEventDataStore.getEvents(it) }
                .wrap(errorHandler.getHandler())
                .toObservable()
    }

    private fun saveEvents(events: List<Event>?, clear: Boolean = false) {
        Completable.fromCallable {
            localEventsDataStore.saveEvents(events, clear)
        }.wrap(errorHandler.getHandler()).subscribe()
    }

}