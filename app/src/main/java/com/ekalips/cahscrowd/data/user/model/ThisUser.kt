package com.ekalips.cahscrowd.data.user.model

interface ThisUser : BaseUser {

    var accessToken: String
    var deviceToken: String?

}