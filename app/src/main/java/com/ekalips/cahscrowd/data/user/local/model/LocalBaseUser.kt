package com.ekalips.cahscrowd.data.user.local.model

import com.ekalips.cahscrowd.data.user.model.BaseUser
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
open class LocalBaseUser(@Id var boxId: Long = 0,
                         override var id: String,
                         override var name: String,
                         override var avatar: String?) : BaseUser