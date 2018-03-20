package com.ekalips.cahscrowd.data.event.remote

import android.util.Log
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.network.Api
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteEventDataStore @Inject constructor(private val api: Api) {

    fun getEvents(token: String, afterEventId: String?, pageSize: Int): Single<List<Event>> {
        Log.e(javaClass.simpleName, "GET EVENTS")
        return Single.fromCallable {
            (0..pageSize).map {
                RemoteEvent("id:${System.currentTimeMillis()}",
                        "test ${System.currentTimeMillis()}", "desc")
            } as List<Event>
        }.delay(2, TimeUnit.SECONDS)
    }

}