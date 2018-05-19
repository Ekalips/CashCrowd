package com.ekalips.cahscrowd.create_event.mvvm.model

import com.ekalips.cahscrowd.data.user.model.BaseUser

data class GuestUserWrap(override var id: String,
                         override var name: String,
                         override var avatar: String?,
                         override var loaded: Boolean,
                         var email: String) : BaseUser