package com.ekalips.cahscrowd.data.user.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.ekalips.cahscrowd.data.user.model.BaseUser

@Entity(tableName = "users")
data class LocalBaseUser(@PrimaryKey
                         @ColumnInfo(name = "userId")
                         override var id: String,
                         @ColumnInfo(name = "userName")
                         override var name: String,
                         override var avatar: String?,
                         override var loaded: Boolean) : BaseUser

fun BaseUser.toLocal() = LocalBaseUser(this.id, this.name, this.avatar, this.loaded)