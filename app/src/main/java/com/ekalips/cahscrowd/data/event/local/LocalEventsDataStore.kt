package com.ekalips.cahscrowd.data.event.local

import android.arch.paging.DataSource
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.paginate.EventsPaginateDataSource
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEventsDataStore @Inject constructor(private val actionsBox: Box<LocalAction>,
                                               private val box: Box<LocalEvent>) {


    fun getEventsDataSourceFactory() = DataSource.Factory<Int, Event> {
        EventsPaginateDataSource(box as Box<Event>, actionsBox as Box<Action>)
    }

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