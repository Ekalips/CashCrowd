package com.ekalips.cahscrowd.data.action.local

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import com.ekalips.cahscrowd.data.user.model.BaseUser
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalActionsDataSource @Inject constructor(private val userBox: Box<LocalBaseUser>,
                                                 private val box: Box<LocalAction>) {

    fun getActions(): Observable<List<Action>> {
        return Observable.combineLatest(RxQuery.observable(box.query().build()).map { it as List<Action> }, RxQuery.observable(userBox.query().build()).map { it as List<BaseUser> },
                BiFunction<List<Action>, List<BaseUser>, List<Action>> { actions, users ->
                    for (action in actions) {
                        action.user = users.firstOrNull { it.id == action.userId }
                    }
                    actions
                })
    }

}