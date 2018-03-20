package com.ekalips.cahscrowd.data.action.local

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.event.local.LocalEvent
import com.ekalips.cahscrowd.data.user.model.BaseUser
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class LocalAction(@Id(assignable = true) var boxId: Long = 0,
                       override var id: String,
                       override var name: String,
                       override var amount: Double,
                       override var userId: String,
                       override var eventId: String,
                       @Transient override var user: BaseUser? = null) : Action {
    lateinit var boxEvent: ToOne<LocalEvent>

}

fun Action.toLocal() = LocalAction(0, id, name, amount, userId, eventId, user)