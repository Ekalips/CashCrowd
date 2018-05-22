package com.ekalips.cahscrowd.data.action.remote

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.network.Api
import com.ekalips.cahscrowd.network.body.CreateActionBody
import com.ekalips.cahscrowd.stuff.ServerError
import io.reactivex.Single
import javax.inject.Inject

class RemoteActionsDataSource @Inject constructor(private val api: Api) {


    fun getActionsForEvent(token: String, eventId: String): Single<List<Action>> {
        return Single.fromCallable {
            val response = api.getEventActions(token, eventId).execute()
            if (response.isSuccessful) {
                return@fromCallable response.body()!!
            }
            throw ServerError(response.code())
        }
    }

    fun createAction(token: String, eventId: String, amount: Double): Single<Action> {
        return Single.fromCallable {
            val response = api.createAction(token, eventId, CreateActionBody(amount)).execute()
            if (response.isSuccessful) {
                return@fromCallable response.body()!!
            }
            throw ServerError(response.code())
        }
    }

}