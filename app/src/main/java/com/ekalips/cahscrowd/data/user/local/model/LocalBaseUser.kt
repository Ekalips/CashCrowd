package com.ekalips.cahscrowd.data.user.local.model

import com.ekalips.cahscrowd.data.user.model.BaseUser
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class LocalBaseUser(@Id(assignable = true) var boxId: Long = 0,
                         override var id: String,
                         override var name: String,
                         override var avatar: String?) : BaseUser

fun BaseUser.toLocal() = LocalBaseUser(0, this.id, this.name, this.avatar)