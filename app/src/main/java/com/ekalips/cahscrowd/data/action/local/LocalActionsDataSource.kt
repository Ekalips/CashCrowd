package com.ekalips.cahscrowd.data.action.local

import android.arch.lifecycle.LiveData
import com.ekalips.base.stuff.ifThen
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.db.CashDB
import com.ekalips.cahscrowd.data.user.local.LocalUserDao
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
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

    fun getActionsForEventLiveData(eventId: String): LiveData<List<Action>>{
        return actionsDao.getActionsForEventLiveData(eventId) as LiveData<List<Action>>

    }


}