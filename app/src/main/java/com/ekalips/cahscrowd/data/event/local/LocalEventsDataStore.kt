package com.ekalips.cahscrowd.data.event.local

import android.arch.paging.DataSource
import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.action.local.toLocal
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.paginate.EventsPaginateDataSource
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEventsDataStore @Inject constructor(private val boxStore: BoxStore,
                                               private val actionsBox: Box<LocalAction>,
                                               private val box: Box<LocalEvent>) {


    fun getEventsDataSourceFactory() = DataSource.Factory<Int, Event> {
        EventsPaginateDataSource(box as Box<Event>, boxStore)
    }

    fun saveEvents(events: List<Event>?, clear: Boolean = false) {
        events?.let {
            val mapped = events.map { it.toLocal() }
            val actions = ArrayList<LocalAction>()
            if (clear) {
                box.store.runInTx {
                    box.removeAll()
                    actionsBox.removeAll()
                }
            } else {
                val currentEvents = box.all
                val currentActions = actionsBox.all
                mapped.forEach {
                    it.boxId = currentEvents.find { local -> it.id == local.id }?.boxId ?: 0
                    it.actions?.let { actions.addAll(it.map { it.toLocal().also { it.boxId = currentActions.find { local -> it.id == local.id }?.boxId ?: 0 } }) }
                }
            }
            box.store.callInTx {
                box.put(mapped)
                actionsBox.put(actions)
            }
        }
    }

}