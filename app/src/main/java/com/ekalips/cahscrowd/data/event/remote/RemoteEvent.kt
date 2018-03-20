package com.ekalips.cahscrowd.data.event.remote

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.event.Event

data class RemoteEvent(override var id: String,
                       override var name: String,
                       override var description: String,
                       override var actions: List<Action>? = emptyList()) : Event