package com.ekalips.cahscrowd.data.action.local

import android.arch.persistence.room.*
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.event.local.LocalEvent
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import com.ekalips.cahscrowd.data.user.local.model.toLocal
import com.ekalips.cahscrowd.data.user.model.BaseUser

@Entity(tableName = "actions", foreignKeys = [
    (ForeignKey(
            entity = LocalBaseUser::class,
            onUpdate = ForeignKey.CASCADE,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("relatedUserId"),
            deferred = true)),
    (ForeignKey(entity = LocalEvent::class,
            onUpdate = ForeignKey.CASCADE,
            parentColumns = arrayOf("eventId"),
            childColumns = arrayOf("relatedEventId"),
            deferred = true))],
        indices = [(Index("relatedUserId")), (Index("relatedEventId"))])
data class LocalAction(@PrimaryKey
                       @ColumnInfo(name = "actionId")
                       override var id: String,
                       @ColumnInfo(name = "actionName")
                       override var name: String,
                       override var amount: Double,
                       @ColumnInfo(name = "relatedUserId")
                       override var userId: String,
                       @ColumnInfo(name = "relatedEventId")
                       override var eventId: String,
                       override var newAction: Boolean) : Action {

    @Embedded
    var localUser: LocalBaseUser? = null

    override var user: BaseUser?
        get() = localUser
        set(value) {
            localUser = value?.toLocal()
        }

    constructor() : this("", "", 0.0, "", "", true)

}

fun Action.toLocal() = LocalAction(id, name, amount, userId, eventId, newAction)