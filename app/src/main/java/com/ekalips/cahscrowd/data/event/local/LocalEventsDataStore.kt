package com.ekalips.cahscrowd.data.event.local

import com.ekalips.base.stuff.ifThen
import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.action.local.toLocal
import com.ekalips.cahscrowd.data.db.CashDB
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.user.local.LocalUserDao
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEventsDataStore @Inject constructor(private val cashDB: CashDB,
                                               private val eventsDao: LocalEventsDao,
                                               private val usersDao: LocalUserDao) {


    fun getEventsDataSourceFactory() = eventsDao.getAllEventsDataSource()

    fun saveEvents(events: List<Event>?, clear: Boolean = false) {
        events?.let {
            val actions = ArrayList<LocalAction>()
            val mappedEvents = ArrayList<LocalEvent>()
            events.forEach {
                actions.addAll((it.actions ?: ArrayList()).map { it.toLocal() })
                mappedEvents.add(it.toLocal())
            }

            val users = usersDao.getAllUsers()

            val absentUserIds = ArrayList<String>(users.size)
            actions.forEach {
                val user = users.find { local -> it.userId == local.id }
                if (user == null) {
                    absentUserIds.add(it.userId)
                }
            }


            cashDB.runInTransaction {
                clear.ifThen { eventsDao.deleteAll() }
                eventsDao.insert(*mappedEvents.toTypedArray())
                eventsDao.insertEventActions(*actions.toTypedArray())
                usersDao.insertUsers(*absentUserIds.map { LocalBaseUser(it, "", null, false) }.toTypedArray())
            }
        }
    }

}