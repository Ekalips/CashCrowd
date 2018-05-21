package com.ekalips.cahscrowd.data.action

import com.ekalips.cahscrowd.data.action.local.LocalActionsDataSource
import com.ekalips.cahscrowd.data.action.remote.RemoteActionsDataSource
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.stuff.ErrorHandler
import com.ekalips.cahscrowd.stuff.utils.wrap
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

    fun saveActionsForEvent(eventId: String, vararg actions: Action, clear: Boolean = false) {
        localActionsDataSource.saveActionsForEvent(eventId, *actions, clear = clear)
    }

}