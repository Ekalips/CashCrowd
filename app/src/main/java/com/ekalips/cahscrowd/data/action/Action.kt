package com.ekalips.cahscrowd.data.action

import com.ekalips.cahscrowd.data.user.model.BaseUser

interface Action {

    var id: String
    var name: String
    var amount: Double
    var userId: String
    var user: BaseUser?
    var eventId: String
    var seen: Boolean

}