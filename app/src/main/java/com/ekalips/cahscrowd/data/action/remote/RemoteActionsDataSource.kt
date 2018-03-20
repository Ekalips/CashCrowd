package com.ekalips.cahscrowd.data.action.remote

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.network.Api
import io.reactivex.Single
import javax.inject.Inject

class RemoteActionsDataSource @Inject constructor(private val api: Api) {


    fun getActionsForEvent(eventId: String): Single<Action> {
        return Single.never() // todo Implement api call when api available
    }

}