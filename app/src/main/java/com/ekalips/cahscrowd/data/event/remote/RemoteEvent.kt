package com.ekalips.cahscrowd.data.event.remote

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.remote.RemoteAction
import com.ekalips.cahscrowd.data.event.Event
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteEvent(@Json(name = "_id") override var id: String,
                       @Json(name = "title") override var name: String = "title",
                       @Json(name = "updatedAt") override var lastUpdate: Long = 0,
                       @Json(name = "amount") override var totalAmount: Double = 0.0,
                       @Json(name = "description") override var description: String = "") : Event {

    @Json(name = "actions")
    internal var remoteActions: List<RemoteAction>? = emptyList()
        set(value) {
            actions = value
        }

    @Transient
    override var actions: List<Action>? = emptyList()
}