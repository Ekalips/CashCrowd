package com.ekalips.cahscrowd.data.user.remote.model

import com.ekalips.cahscrowd.data.user.model.ThisUser
import com.squareup.moshi.Json

data class RemoteThisUser(@Json(name = "token") override var accessToken: String,
                          override var deviceToken: String?,
                          var user: RemoteBaseUser) : ThisUser {
    override var loaded: Boolean
        get() = user.loaded
        set(value) {
            user.loaded = value
        }
    override var id: String
        get() = user.id
        set(value) {
            user.id = value
        }
    override var name: String
        get() = user.name
        set(value) {
            user.name = name
        }
    override var avatar: String?
        get() = user.avatar
        set(value) {
            user.avatar
        }
}