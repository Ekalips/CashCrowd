package com.ekalips.cahscrowd.data.action.local

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface LocalActionsDao {
    @Query("SELECT * FROM actions")
    fun getActions(): List<LocalAction>

    @Query("SELECT * FROM actions")
    fun getActionsAsync(): Flowable<List<LocalAction>>

    @Query("SELECT * FROM actions WHERE id = :id")
    fun getAction(id: String): LocalAction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAction(vararg users: LocalAction)

    @Query("DELETE FROM actions")
    fun deleteAll()

    @Query("DELETE FROM actions WHERE id IN(:ids)")
    fun delete(vararg ids: String)

    @Delete
    fun delete(vararg action: LocalAction)
}