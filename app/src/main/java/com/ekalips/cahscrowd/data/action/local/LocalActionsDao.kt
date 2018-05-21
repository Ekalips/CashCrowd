package com.ekalips.cahscrowd.data.action.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface LocalActionsDao {

    @Query("SELECT * FROM actions INNER JOIN users ON actions.relatedUserId = users.userId WHERE relatedEventId = :eventId")
    fun getActionsForEvent(eventId: String): LiveData<List<LocalAction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg actions: LocalAction)

    @Query("DELETE FROM actions")
    fun deleteAll()

    @Query("DELETE FROM actions WHERE actionId IN(:ids)")
    fun delete(vararg ids: String)

    @Delete
    fun delete(vararg action: LocalAction)
}