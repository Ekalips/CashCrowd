package com.ekalips.cahscrowd.data.action

import android.arch.lifecycle.LiveData
import com.ekalips.cahscrowd.data.action.local.LocalActionsDataSource
import com.ekalips.cahscrowd.data.action.remote.RemoteActionsDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionsDataProvider @Inject constructor(private val localActionsDataSource: LocalActionsDataSource,
                                              private val remoteActionsDataSource: RemoteActionsDataSource) {

    fun getActionsForEvent(eventId: String): LiveData<List<Action>> {
        return localActionsDataSource.getActionsForEvent(eventId)
    }

}