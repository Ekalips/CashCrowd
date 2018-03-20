package com.ekalips.cahscrowd.data.event.local

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.action.local.toLocal
import com.ekalips.cahscrowd.data.event.Event
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class LocalEvent(@Id var boxId: Long = 0,
                      override var id: String,
                      override var name: String,
                      override var description: String) : Event {

    @Backlink
    var actionsInternal: List<LocalAction>? = emptyList()

    override var actions: List<Action>?
        get() = actionsInternal
        set(value) {
            actionsInternal = value?.map { it.toLocal() }  // todo Regret this later maybe
        }

    constructor() : this(0, "", "", "")

}

fun Event.toLocal() = LocalEvent(0, id, name, description).also { local -> local.actions = this.actions }