package com.ekalips.cahscrowd.data.action.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.user.model.BaseUser

@Entity(tableName = "actions")
data class LocalAction(@PrimaryKey override var id: String,
                       override var name: String,
                       override var amount: Double,
                       override var userId: String,
                       override var eventId: String,
                       @Transient override var user: BaseUser? = null) : Action {

    constructor() : this("", "", 0.0, "", "")

}