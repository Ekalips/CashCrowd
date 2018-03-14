package com.ekalips.cahscrowd.data.user.remote.model

import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.squareup.moshi.Json

data class RemoteBaseUser(@Json(name = "_id") override var id: String,
                          @Json(name = "name") override var name: String,
                          @Json(name = "avatar") override var avatar: String?) : BaseUser