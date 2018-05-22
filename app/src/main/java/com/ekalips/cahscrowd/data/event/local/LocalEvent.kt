package com.ekalips.cahscrowd.data.event.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.event.Event

@Entity(tableName = "events")
open class LocalEvent(
        @ColumnInfo(name = "eventId")
        @PrimaryKey
        override var id: String,
        @ColumnInfo(name = "eventName")
        override var name: String,
        @ColumnInfo(name = "eventDescription")
        override var description: String,
        @Ignore override var actions: List<Action>?) : Event {

    constructor() : this("", "", "", emptyList())

}

fun Event.toLocal() = LocalEvent(id, name, description, actions)