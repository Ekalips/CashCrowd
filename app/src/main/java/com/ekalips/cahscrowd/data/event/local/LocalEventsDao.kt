package com.ekalips.cahscrowd.data.event.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface LocalEventsDao {

    @Query("SELECT * FROM events ORDER BY events.lastUpdate DESC")
    fun getEvents(): List<LocalEvent>

    @Query("DELETE FROM events")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg events: LocalEvent)

    @Transaction
    @Query("SELECT * FROM events where eventId = :eventId")
    fun getEvent(eventId: String): LiveData<LocalEvent>

    @Transaction
    @Query("SELECT * FROM events ORDER BY events.lastUpdate DESC")
    fun getEventsLiveData(): LiveData<List<LocalEvent>>
}