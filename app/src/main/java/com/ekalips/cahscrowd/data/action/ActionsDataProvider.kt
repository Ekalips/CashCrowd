package com.ekalips.cahscrowd.data.action

import com.ekalips.cahscrowd.data.action.local.LocalActionsDataSource
import com.ekalips.cahscrowd.data.action.remote.RemoteActionsDataSource
import com.ekalips.cahscrowd.stuff.utils.wrap
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionsDataProvider @Inject constructor(private val localActionsDataSource: LocalActionsDataSource,
                                              private val remoteActionsDataSource: RemoteActionsDataSource) {

    fun saveActions(vararg actions: Action) {
        Completable.fromAction {
            localActionsDataSource.saveActions(*actions)
        }.wrap().subscribe()
    }

}