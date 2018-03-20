package com.ekalips.cahscrowd.data.action.local

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.user.local.LocalUserDao
import com.ekalips.cahscrowd.data.user.model.BaseUser
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalActionsDataSource @Inject constructor(private val userDao: LocalUserDao,
                                                 private val actionsDao: LocalActionsDao) {

    fun getActions(): Observable<List<Action>> {
        return Observable.combineLatest(actionsDao.getActionsAsync().toObservable(), userDao.getUsersAsync().toObservable(),
                BiFunction<List<Action>, List<BaseUser>, List<Action>> { actions, users ->
                    for (action in actions) {
                        action.user = users.firstOrNull { it.id == action.userId }
                    }
                    actions
                })
    }

}