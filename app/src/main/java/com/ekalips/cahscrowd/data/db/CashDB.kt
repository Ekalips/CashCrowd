package com.ekalips.cahscrowd.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.action.local.LocalActionsDao
import com.ekalips.cahscrowd.data.event.local.LocalEvent
import com.ekalips.cahscrowd.data.event.local.LocalEventsDao
import com.ekalips.cahscrowd.data.user.local.LocalUserDao
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser

@Database(version = 13,
        exportSchema = false,
        entities = [LocalAction::class, LocalEvent::class, LocalBaseUser::class])
abstract class CashDB : RoomDatabase() {

    companion object {
        fun create(context: Context): CashDB {
            return Room.databaseBuilder(context, CashDB::class.java, "cash.db")
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }

    abstract fun users(): LocalUserDao
    abstract fun events(): LocalEventsDao
    abstract fun actions(): LocalActionsDao
}