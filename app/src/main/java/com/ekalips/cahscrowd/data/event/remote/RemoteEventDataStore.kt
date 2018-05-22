package com.ekalips.cahscrowd.data.event.remote

import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.network.Api
import com.ekalips.cahscrowd.network.body.CreateEventBody
import com.ekalips.cahscrowd.stuff.ServerError
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteEventDataStore @Inject constructor(private val api: Api) {

//    private val mockData = listOf(
//            RemoteEvent("event1", "name1", "desc1",
//                    listOf(
//                            RemoteAction("act1_1", "action1_1", 44.0, "user1", null, "event1", true),
//                            RemoteAction("act1_2", "action1_2", 44.0, "user1", null, "event1", true),
//                            RemoteAction("act1_3", "action1_3", 44.0, "user2", null, "event1", false),
//                            RemoteAction("act1_4", "action1_4", 44.0, "user3", null, "event2", false),
//                            RemoteAction("act1_5", "action1_5", 44.0, "user4", null, "event2", false)
//                    )),
//            RemoteEvent("event2", "name2", "desc2", listOf()),
//            RemoteEvent("event3", "name3", "desc3", listOf()),
//            RemoteEvent("event4", "name4", "desc4", listOf()),
//            RemoteEvent("event5", "name5", "desc5", listOf())
//    )

    fun getEvents(token: String): Single<List<Event>> {
        return Single.fromCallable {
            val result = api.getEvents(token).execute()
            if (result.isSuccessful) {
                return@fromCallable result.body()!!.also { it.forEach { event -> event.actions?.forEach { it.eventId = event.id } } }
            }
            throw ServerError(result.code())
        }
    }

    fun crateEvent(token: String, title: String, description: String): Single<Event> {
        return Single.fromCallable {
            val result = api.createEvent(token, CreateEventBody(title, description)).execute()
            if (result.isSuccessful) {
                return@fromCallable result.body()!!
            }
            throw ServerError(result.code())
        }
    }

}