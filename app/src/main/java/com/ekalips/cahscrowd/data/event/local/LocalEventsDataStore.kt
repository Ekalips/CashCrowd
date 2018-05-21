package com.ekalips.cahscrowd.data.event.local

import android.arch.lifecycle.LiveData
import android.util.Log
import com.ekalips.base.stuff.ifThen
import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.action.local.LocalActionsDao
import com.ekalips.cahscrowd.data.action.local.toLocal
import com.ekalips.cahscrowd.data.db.CashDB
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.user.local.LocalUserDao
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEventsDataStore @Inject constructor(private val cashDB: CashDB,
                                               private val eventsDao: LocalEventsDao,
                                               private val actionsDao: LocalActionsDao,
                                               private val usersDao: LocalUserDao) {


    fun getEventsDataSourceFactory() = eventsDao.getAllEventsDataSource()

    fun getEvents() = Observable.fromCallable { eventsDao.getEvents() } as Observable<List<Event>>

    fun saveEvents(events: List<Event>?, clear: Boolean = false) {
        events?.let { Log.d(javaClass.simpleName, "Save Events: $it") }
        events?.let {
            val actions = ArrayList<LocalAction>()
            val mappedEvents = ArrayList<LocalEvent>()
            events.forEach {
                actions.addAll((it.actions ?: ArrayList()).map { it.toLocal() })
                mappedEvents.add(it.toLocal().also { it.actions = null })
            }

            val users = usersDao.getAllUsers()
            val absentUserIds = HashSet<String>(users.size)
            actions.forEach {
                users.find { local -> it.userId == local.id } ?: absentUserIds.add(it.userId)
            }


            cashDB.runInTransaction {
                clear.ifThen { eventsDao.deleteAll() }
                eventsDao.insert(*mappedEvents.toTypedArray())
                actionsDao.insert(*actions.toTypedArray())
                usersDao.insert(*absentUserIds.map { LocalBaseUser(it, "", null, false) }.toTypedArray())
            }
        }
    }

    fun getEvent(eventId: String): LiveData<Event> = eventsDao.getEvent(eventId) as LiveData<Event>

}