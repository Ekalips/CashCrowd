package com.ekalips.cahscrowd.data.user.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser

@Dao
interface LocalUserDao {
    @Transaction
    @Query("SELECT * FROM users WHERE users.userId IN(:ids)")
    fun getUsers(vararg ids: String): LiveData<List<LocalBaseUser>>

    @Query("SELECT * FROM users WHERE userId = :uId")
    fun getUser(uId: String): LocalBaseUser?

    @Query("SELECT * FROM users WHERE users.userId = :userId")
    fun getUserLiveData(userId: String): LiveData<LocalBaseUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: LocalBaseUser)

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<LocalBaseUser>

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("DELETE FROM users WHERE userId IN(:ids)")
    fun delete(vararg ids: String)

    @Delete
    fun delete(vararg user: LocalBaseUser)
}