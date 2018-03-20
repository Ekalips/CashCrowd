package com.ekalips.cahscrowd.data.event.local

import android.arch.lifecycle.LiveData
import android.util.Log
import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.paginate.EventsDataSourceFactory
import io.objectbox.Box
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.rx.RxQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEventsDataStore @Inject constructor(private val actionsBox: Box<LocalAction>,
                                               private val box: Box<LocalEvent>) {

    init {
        RxQuery.observable(box.query().build()).subscribe { Log.d(javaClass.simpleName, "eventsCount: ${it.size}") }
    }

    fun getEvents(): LiveData<List<LocalEvent>> {
        return ObjectBoxLiveData<LocalEvent>(box.query().build())
    }

    fun getEventsDataSourceFactory() = EventsDataSourceFactory(box)

    fun saveEvents(events: List<Event>?, clear: Boolean = false) {
        events?.let {
            val mapped = events.map { it.toLocal() }
            if (clear) {
                box.removeAll()
            } else {
                val currentEvents = box.all
                mapped.forEach {
                    it.boxId = currentEvents.find { local -> it.id == local.id }?.boxId ?: 0
                }
            }
            box.put(mapped)
        }
    }

}