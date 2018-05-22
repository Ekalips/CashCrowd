package com.ekalips.cahscrowd.data.action.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface LocalActionsDao {

    @Query("SELECT * FROM actions INNER JOIN users ON actions.relatedUserId = users.userId WHERE relatedEventId = :eventId")
    fun getActionsForEvent(eventId: String): List<LocalAction>

    @Query("SELECT * FROM actions INNER JOIN users ON actions.relatedUserId = users.userId WHERE relatedEventId = :eventId")
    fun getActionsForEventLiveData(eventId: String): LiveData<LocalAction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg actions: LocalAction)

    @Query("DELETE FROM actions")
    fun deleteAll()

    @Query("DELETE FROM actions WHERE actionId IN(:ids)")
    fun delete(vararg ids: String)

    @Query("DELETE FROM actions WHERE relatedEventId = :eventId")
    fun deleteEventActions(eventId: String)

    @Delete
    fun delete(vararg action: LocalAction)
}