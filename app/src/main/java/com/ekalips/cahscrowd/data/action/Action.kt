package com.ekalips.cahscrowd.data.action

import com.ekalips.cahscrowd.data.user.model.BaseUser

interface Action {

    var id: String
    var name: String
    var amount: Double
    var userId: String
    var user: BaseUser?
    var eventId: String
    var newAction: Boolean

    companion object {
        fun compare(action1: Action?, action2: Action?): Boolean {
            return action1?.id == action2?.id &&
                    action1?.name == action2?.name &&
                    action1?.userId == action2?.userId &&
                    action1?.amount == action2?.amount &&
                    action1?.eventId == action2?.eventId &&
                    action1?.user == action2?.user &&
                    action1?.newAction == action2?.newAction
        }

    }
}