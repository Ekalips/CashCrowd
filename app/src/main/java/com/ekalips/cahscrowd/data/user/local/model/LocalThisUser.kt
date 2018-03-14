package com.ekalips.cahscrowd.data.user.local.model

import com.ekalips.cahscrowd.data.user.model.ThisUser

class LocalThisUser(override var id: String,
                    override var name: String,
                    override var avatar: String?,
                    override var accessToken: String,
                    override var deviceToken: String?) : ThisUser