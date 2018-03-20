package com.ekalips.cahscrowd.data.user.local.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.ekalips.cahscrowd.data.user.model.BaseUser

@Entity(tableName = "users")
data class LocalBaseUser(@PrimaryKey override var id: String,
                         override var name: String,
                         override var avatar: String?) : BaseUser

fun BaseUser.toLocal() = LocalBaseUser(this.id, this.name, this.avatar)