package com.ekalips.cahscrowd.data.event.remote

import android.util.Log
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.network.Api
import io.reactivex.Single
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteEventDataStore @Inject constructor(private val api: Api) {

    fun getEvents(token: String, afterEventId: String?, pageSize: Int): Single<List<Event>> {
        Log.e(javaClass.simpleName, "GET EVENTS")

        val random = ThreadLocalRandom.current()
        return Single.fromCallable {
            (0 until pageSize).map {
                RemoteEvent("id:${random.nextLong(5000L)}",
                        "test ${random.nextLong(5000L)}", "desc")
            } as List<Event>
        }.delay(2, TimeUnit.SECONDS)
    }

}