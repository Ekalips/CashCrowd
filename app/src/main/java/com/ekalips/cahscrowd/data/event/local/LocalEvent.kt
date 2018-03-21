package com.ekalips.cahscrowd.data.event.local

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.event.Event
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class LocalEvent(@Id var boxId: Long = 0,
                      override var id: String,
                      override var name: String,
                      override var description: String,
                      override var actions: List<Action>? = emptyList()) : Event {

    constructor() : this(0, "", "", "")

}

fun Event.toLocal() = LocalEvent(0, id, name, description).also { local -> local.actions = this.actions }