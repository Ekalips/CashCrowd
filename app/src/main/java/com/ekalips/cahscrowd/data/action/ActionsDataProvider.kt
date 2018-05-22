package com.ekalips.cahscrowd.data.action

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.ekalips.cahscrowd.data.action.local.LocalActionsDataSource
import com.ekalips.cahscrowd.data.action.remote.RemoteActionsDataSource
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.stuff.ErrorHandler
import com.ekalips.cahscrowd.stuff.data.Listing
import com.ekalips.cahscrowd.stuff.paging.NetworkState
import com.ekalips.cahscrowd.stuff.utils.wrap
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionsDataProvider @Inject constructor(private val userDataProvider: UserDataProvider,
                                              private val localActionsDataSource: LocalActionsDataSource,
                                              private val remoteActionsDataSource: RemoteActionsDataSource,
                                              private val errorHandler: ErrorHandler) {

    fun getActionsForEvent(eventId: String): Observable<List<Action>> {
        return Observable.concatDelayError(Observable.just(localActionsDataSource.getActionsForEvent(eventId),
                userDataProvider.getAccessToken().flatMap { remoteActionsDataSource.getActionsForEvent(it, eventId) }.toObservable()
                        .doOnNext { saveActionsForEvent(eventId, *it.toTypedArray(), clear = true) }))
                .wrap(errorHandler.getHandler())
    }

    fun getActionsForEventLiveData(eventId: String): Listing<Action> {
        val data = localActionsDataSource.getActionsForEventLiveData(eventId)
        val refreshTrigger = SingleLiveEvent<Void>()
        val networkState = Transformations.switchMap(refreshTrigger, { fetchActionsRemotely(eventId) })
        return Listing(data = data, networkState = networkState, refresh = { refreshTrigger.call() }, retry = { refreshTrigger.call() })
    }

    private fun fetchActionsRemotely(eventId: String): LiveData<NetworkState> {
        val state = MutableLiveData<NetworkState>()
        state.value = NetworkState.LOADING
        userDataProvider.getAccessToken().flatMap { remoteActionsDataSource.getActionsForEvent(it, eventId) }
                .subscribe({
                    saveActionsForEvent(eventId, *it.toTypedArray(), clear = true)
                    state.postValue(NetworkState.LOADED)
                }, { state.postValue(NetworkState.error(it.message)) })
        return state
    }

    fun saveActionsForEvent(eventId: String, vararg actions: Action, clear: Boolean = false) {
        localActionsDataSource.saveActionsForEvent(eventId, *actions, clear = clear)
    }

    fun createAction(eventId: String, amount: Double): Completable {
        return Completable.fromSingle(userDataProvider.getAccessToken().flatMap { remoteActionsDataSource.createAction(it, eventId, amount) }
                .doOnSuccess { saveActionsForEvent(eventId, it, clear = false) })
                .wrap(errorHandler.getHandler())
    }

}