package com.ekalips.cahscrowd.data.action.remote

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteAction(@Json(name = "_id") override var id: String,
                        @Json(name = "comment") override var name: String = "",
                        @Json(name = "amount") override var amount: Double,
                        @Json(name = "author") override var userId: String,
                        @Transient override var user: BaseUser? = null,
                        @Json(name = "event") override var eventId: String = "",
                        @Json(name = "is_new") override var newAction: Boolean = false) : Action