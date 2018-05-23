package com.ekalips.cahscrowd.data.action.local

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import com.ekalips.base.stuff.ifThen
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.db.CashDB
import com.ekalips.cahscrowd.data.user.local.LocalUserDao
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import com.ekalips.cahscrowd.data.user.model.BaseUser
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalActionsDataSource @Inject constructor(private val cashDB: CashDB,
                                                 private val usersDao: LocalUserDao,
                                                 private val actionsDao: LocalActionsDao) {

    fun getActionsForEvent(eventId: String): Observable<List<Action>> {
        return Observable.fromCallable { actionsDao.getActionsForEvent(eventId) } as Observable<List<Action>>
    }

    fun saveActionsForEvent(eventId: String, vararg actions: Action, clear: Boolean = false) {
        val mappedActions = actions.map { it.toLocal() }

        val users = usersDao.getAllUsers()
        val absentUserIds = HashSet<String>(users.size)
        actions.forEach {
            users.find { local -> it.userId == local.id } ?: absentUserIds.add(it.userId)
        }

        cashDB.runInTransaction {
            clear.ifThen { actionsDao.deleteEventActions(eventId) }
            actionsDao.insert(*mappedActions.toTypedArray())
            usersDao.insert(*absentUserIds.map { LocalBaseUser(it, "", null, false) }.toTypedArray())
        }

    }

    fun getActionsForEventLiveData(eventId: String): LiveData<List<Action>> {
        val actionsLiveData = actionsDao.getActionsForEventLiveData(eventId)
        val usersLiveData = Transformations.switchMap(actionsLiveData, {
            val userIds = it.map { it.userId }
            usersDao.getUsers(*userIds.toTypedArray())
        })
        val result = MediatorLiveData<List<Action>>()

        fun mapUsersToActions(actions: List<Action>?, users: List<BaseUser>?): List<Action>? {
            println("mapUsersToActions_Actions: $actions")
            println("mapUsersToActions_Users: $users")
            return if (actions == null) {
                null
            } else {
                actions.forEach { action ->
                    action.user = users?.find { it.id == action.userId }
                }
                actions
            }
        }

        result.addSource(actionsLiveData, {
            result.postValue(mapUsersToActions(actionsLiveData.value, usersLiveData.value))
        })
        result.addSource(usersLiveData, {
            result.postValue(mapUsersToActions(actionsLiveData.value, usersLiveData.value))
        })
        return result
    }


}