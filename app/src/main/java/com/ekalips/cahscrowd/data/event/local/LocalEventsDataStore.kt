package com.ekalips.cahscrowd.data.event.local

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import com.ekalips.base.stuff.ifThen
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.action.local.LocalActionsDao
import com.ekalips.cahscrowd.data.action.local.toLocal
import com.ekalips.cahscrowd.data.db.CashDB
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.user.local.LocalUserDao
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.stuff.other.post
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEventsDataStore @Inject constructor(private val cashDB: CashDB,
                                               private val eventsDao: LocalEventsDao,
                                               private val actionsDao: LocalActionsDao,
                                               private val usersDao: LocalUserDao) {

    fun getEventsLiveData(): LiveData<List<Event>> {
        val originEvents = eventsDao.getEventsLiveData()
        val stuff = Transformations.map(originEvents, {
            it.map { fillEvent(it) }
        })
        val result = MediatorLiveData<List<Event>>()

        fun validateData(){
            val data = stuff.value?.map { it.value }
            result post data?.filterNotNull()
        }

        result.addSource(stuff, {
            val filledLiveData = stuff.value?.map { it }
            filledLiveData?.forEach {
                result.addSource(it, {
                    validateData()
                })
            }
        })

        return result
    }

    fun saveEvents(events: List<Event>?, clear: Boolean = false) {
        events?.let { Log.d(javaClass.simpleName, "Save Events: $it") }
        events?.let {
            val actions = ArrayList<LocalAction>()
            val mappedEvents = ArrayList<LocalEvent>()
            events.forEach { event ->
                actions.addAll((event.actions ?: ArrayList())
                        .map { it.toLocal().also { it.eventId = event.id } })
                mappedEvents.add(event.toLocal().also { it.actions = null })
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

    fun getEvent(eventId: String): LiveData<Event> {
        val event = eventsDao.getEvent(eventId)
        return Transformations.switchMap(event, { fillEvent(it) })
    }

    private fun fillEvent(event: Event): LiveData<Event> {
        val actions = actionsDao.getActionsForEventLiveData(event.id)
        val users = Transformations.switchMap(actions, {
            val ids = it.map { it.userId }
            usersDao.getUsers(*ids.toTypedArray())
        })
        val result = MediatorLiveData<Event>()
        fun mapEventToActionsAndUsers(event: Event?, actions: List<Action>?, users: List<BaseUser>?): Event? {
            return if (event != null) {
                actions?.forEach { action ->
                    action.user = users?.find { it.id == action.userId }
                }
                event.actions = actions
                event
            } else {
                null
            }
        }

        result.addSource(actions, { result post mapEventToActionsAndUsers(event, actions.value, users.value) })
        result.addSource(users, { result post mapEventToActionsAndUsers(event, actions.value, users.value) })
        return result
    }


}