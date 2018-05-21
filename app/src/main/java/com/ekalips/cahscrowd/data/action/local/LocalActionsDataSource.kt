package com.ekalips.cahscrowd.data.action.local

import android.arch.lifecycle.LiveData
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.user.local.LocalUserDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalActionsDataSource @Inject constructor(private val userDao: LocalUserDao,
                                                 private val actionsDao: LocalActionsDao) {

    fun getActionsForEvent(eventId: String): LiveData<List<Action>> {
        return actionsDao.getActionsForEvent(eventId) as LiveData<List<Action>>
    }

}