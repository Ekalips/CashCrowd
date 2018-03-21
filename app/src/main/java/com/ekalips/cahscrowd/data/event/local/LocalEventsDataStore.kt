package com.ekalips.cahscrowd.data.event.local

import android.arch.paging.DataSource
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.local.LocalActionsDataSource
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.paginate.EventsPaginateDataSource
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEventsDataStore @Inject constructor(private val boxStore: BoxStore,
                                               private val localActionsDataSource: LocalActionsDataSource,
                                               private val box: Box<LocalEvent>) {


    fun getEventsDataSourceFactory() = DataSource.Factory<Int, Event> {
        EventsPaginateDataSource(box as Box<Event>, boxStore, localActionsDataSource)
    }

    fun saveEvents(events: List<Event>?, clear: Boolean = false) {
        events?.let {
            val mapped = events.map { it.toLocal() }
            val actions = ArrayList<Action>()
            if (clear) {
                box.store.runInTx {
                    box.removeAll()
                    localActionsDataSource.clear()
                }
            } else {
                val currentEvents = box.all
                mapped.forEach {
                    it.boxId = currentEvents.find { local -> it.id == local.id }?.boxId ?: 0
                    it.actions?.let { actions.addAll(it) }

                }
            }
            box.store.callInTx {
                box.put(mapped)
                localActionsDataSource.saveActions(*actions.toTypedArray())
            }
        }
    }

}