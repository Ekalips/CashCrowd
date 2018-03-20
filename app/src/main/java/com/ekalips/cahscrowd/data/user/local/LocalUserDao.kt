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

    @Query("SELECT * FROM users WHERE id = :uId")
    fun getUser(uId: String): LocalBaseUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg users: LocalBaseUser)

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("DELETE FROM users WHERE id IN(:ids)")
    fun delete(vararg ids: String)

    @Delete
    fun delete(vararg user: LocalBaseUser)
}