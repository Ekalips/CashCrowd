package com.ekalips.cahscrowd.data.event.local

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.event.Event

class LocalEventWithActions : Event {

    @Embedded
    lateinit var event: LocalEvent

    @Relation(parentColumn = "eventId",
            entityColumn = "relatedEventId")
    lateinit var localActions: List<LocalAction>

    override var id: String
        get() = event.id
        set(value) {
            event.id = id
        }
    override var name: String
        get() = event.name
        set(value) {
            event.name = value
        }
    override var description: String
        get() = event.description
        set(value) {
            event.description = description
        }
    override var actions: List<Action>?
        get() = localActions
        set(value) {
            throw UnsupportedOperationException()
        }
}