package com.ekalips.cahscrowd.data.action.local

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.user.local.LocalUserDataSource
import com.ekalips.cahscrowd.data.user.model.BaseUser
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalActionsDataSource @Inject constructor(private val localUserDataSource: LocalUserDataSource,
                                                 private val box: Box<LocalAction>) {

    fun getActions(): Observable<List<Action>> {
        return Observable.combineLatest(RxQuery.observable(box.query().build()).map { it as List<Action> }, localUserDataSource.getAll().toObservable(),
                BiFunction<List<Action>, List<BaseUser>, List<Action>> { actions, users -> mapActionsWithUsers(actions, users) })
    }

    fun getActionsForEvent(eventId: String): Observable<List<Action>> {
        return Observable.combineLatest(RxQuery.observable(box.query().equal(LocalAction_.eventId, eventId).build()).map { it as List<Action> },
                localUserDataSource.getAll().toObservable(),
                BiFunction<List<Action>, List<BaseUser>, List<Action>> { actions, users -> mapActionsWithUsers(actions, users) })
    }

    fun saveActions(vararg actions: Action) {
        val mapped = actions.map { it.toLocal() }
        val users = ArrayList<BaseUser>()
        val current = box.all
        mapped.forEach {
            it.boxId = current.find { local -> it.id == local.id }?.boxId ?: 0
            it.user?.let { users.add(it) }
        }
        box.put(mapped)
        localUserDataSource.saveUsers(*users.toTypedArray())
    }

    fun clear() {
        box.removeAll()
    }

    private fun mapActionsWithUsers(actions: List<Action>, users: List<BaseUser>): List<Action> {
        for (action in actions) {
            action.user = users.firstOrNull { it.id == action.userId }
        }
        return actions
    }

}