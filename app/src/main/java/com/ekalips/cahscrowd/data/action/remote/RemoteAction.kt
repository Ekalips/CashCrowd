package com.ekalips.cahscrowd.data.action.remote

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.squareup.moshi.Json

data class RemoteAction(@Json(name = "id") override var id: String,
                        @Json(name = "name") override var name: String,
                        @Json(name = "amount") override var amount: Double,
                        @Json(name = "user") override var userId: String,
                        @Transient override var user: BaseUser?,
                        @Json(name = "event") override var eventId: String = "") : Action