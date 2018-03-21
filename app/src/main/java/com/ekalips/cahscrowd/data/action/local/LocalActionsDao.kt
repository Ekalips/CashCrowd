package com.ekalips.cahscrowd.data.action.local

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface LocalActionsDao {
    @Query("SELECT * FROM actions, users")
    fun getActions(): List<LocalAction>

    @Query("SELECT * FROM actions, users")
    fun getActionsAsync(): Flowable<List<LocalAction>>

    @Query("SELECT * FROM actions,users WHERE actionId = :id")
    fun getAction(id: String): LocalAction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAction(vararg users: LocalAction)

    @Query("DELETE FROM actions")
    fun deleteAll()

    @Query("DELETE FROM actions WHERE actionId IN(:ids)")
    fun delete(vararg ids: String)

    @Delete
    fun delete(vararg action: LocalAction)
}