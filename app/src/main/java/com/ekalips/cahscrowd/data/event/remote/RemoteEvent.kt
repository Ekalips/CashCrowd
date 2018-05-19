package com.ekalips.cahscrowd.data.event.remote

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.remote.RemoteAction
import com.ekalips.cahscrowd.data.event.Event
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class RemoteEvent(@Json(name = "_id") override var id: String,
                       @Json(name = "title") override var name: String,
                       @Json(name = "description") override var description: String = "",
                       @Json(name = "actions") override var actions: List<Action>? = emptyList()) : Event

class ActionsJsonAdapter : JsonAdapter<Action>() {
    private val remoteAdapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<RemoteAction?>(RemoteAction::class.java).nullSafe()

    override fun fromJson(reader: JsonReader?): Action? {
        return remoteAdapter.fromJson(reader)
    }

    override fun toJson(writer: JsonWriter?, value: Action?) {
        value?.let {
            remoteAdapter.toJson(writer, RemoteAction(it.id, it.name, it.amount, it.userId, it.user, it.eventId, it.newAction))
        }
    }
}