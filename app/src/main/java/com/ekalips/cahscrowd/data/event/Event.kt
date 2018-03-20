package com.ekalips.cahscrowd.data.event

import com.ekalips.cahscrowd.data.action.Action

interface Event {

    var id: String
    var name: String
    var description: String
    var actions: List<Action>?

}