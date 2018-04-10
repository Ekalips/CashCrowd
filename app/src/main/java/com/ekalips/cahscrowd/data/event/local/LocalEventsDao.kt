package com.ekalips.cahscrowd.data.event.local

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface LocalEventsDao {


    @Query("SELECT * FROM events")
    fun getAllEventsDataSource(): DataSource.Factory<Int, LocalEventWithActions>

    @Query("DELETE FROM events")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg events: LocalEvent)
}