package com.ekalips.cahscrowd.data.user.local

import android.arch.persistence.room.*
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import io.reactivex.Flowable

@Dao
interface LocalUserDao {
    @Query("SELECT * FROM users")
    fun getUsers(): List<LocalBaseUser>

    @Query("SELECT * FROM users")
    fun getUsersAsync(): Flowable<List<LocalBaseUser>>

    @Query("SELECT * FROM users WHERE userId = :uId")
    fun getUser(uId: String): LocalBaseUser?

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