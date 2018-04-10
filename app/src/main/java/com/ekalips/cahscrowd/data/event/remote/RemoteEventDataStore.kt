package com.ekalips.cahscrowd.data.event.remote

import android.util.Log
import com.ekalips.cahscrowd.data.action.remote.RemoteAction
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.network.Api
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteEventDataStore @Inject constructor(private val api: Api) {

    private val mockData = listOf(
            RemoteEvent("event1", "name1", "desc1",
                    listOf(
                            RemoteAction("act1_1", "action1_1", 44.0, "user1", null, "event1", true),
                            RemoteAction("act1_2", "action1_2", 44.0, "user1", null, "event1", true),
                            RemoteAction("act1_3", "action1_3", 44.0, "user2", null, "event1", false),
                            RemoteAction("act1_4", "action1_4", 44.0, "user3", null, "event2", false),
                            RemoteAction("act1_5", "action1_5", 44.0, "user4", null, "event2", false)
                    )),
            RemoteEvent("event2", "name2", "desc2",
                    listOf()),
            RemoteEvent("event3", "name3", "desc3",
                    listOf()),
            RemoteEvent("event4", "name4", "desc4",
                    listOf()),
            RemoteEvent("event5", "name5", "desc5",
                    listOf())
    )

    fun getEvents(token: String, afterEventId: String?, pageSize: Int): Single<List<Event>> {
        Log.e(javaClass.simpleName, "GET EVENTS")
        return Single.fromCallable {
            if (afterEventId == null) return@fromCallable mockData as List<Event>
            else return@fromCallable ArrayList<Event>()
        }.delay(2, TimeUnit.SECONDS)
    }

}