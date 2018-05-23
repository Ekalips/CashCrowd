package com.ekalips.cahscrowd.data.action.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface LocalActionsDao {

    @Query("SELECT * FROM actions LEFT JOIN users ON actions.relatedUserId = users.userId WHERE relatedEventId = :eventId")
    @Transaction
    fun getActionsForEvent(eventId: String): List<LocalAction>

    @Query("SELECT * FROM actions WHERE relatedEventId = :eventId")
    @Transaction
    fun getActionsForEventLiveData(eventId: String): LiveData<List<LocalAction>>

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