package com.ekalips.cahscrowd.data.event.local

import com.ekalips.cahscrowd.data.action.local.LocalActionsDao
import com.ekalips.cahscrowd.data.event.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEventsDataStore @Inject constructor(private val actionsDao: LocalActionsDao,
                                               private val eventsDao: LocalEventsDao) {


    fun getEventsDataSourceFactory() = eventsDao.getAllEventsDataSource()

    fun saveEvents(events: List<Event>?, clear: Boolean = false) {
        events?.let {
            val mapped = events.map { it.toLocal() }
            if (clear) {
                eventsDao.deleteAll()
            }
            eventsDao.insert(*mapped.toTypedArray())
        }
    }

}