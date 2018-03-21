package com.ekalips.cahscrowd.data.user.model

interface BaseUser {

    var id: String
    var name: String
    var avatar: String?
    var loaded: Boolean

}