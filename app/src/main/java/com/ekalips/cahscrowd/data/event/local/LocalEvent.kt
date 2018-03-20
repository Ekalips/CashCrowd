package com.ekalips.cahscrowd.data.event.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.event.Event

@Entity(tableName = "events")
data class LocalEvent(@PrimaryKey override var id: String,
                      override var name: String,
                      override var description: String,
                      @Transient override var actions: List<Action>?) : Event {

    constructor() : this("", "", "", emptyList())

}

fun Event.toLocal() = LocalEvent(id, name, description, actions)