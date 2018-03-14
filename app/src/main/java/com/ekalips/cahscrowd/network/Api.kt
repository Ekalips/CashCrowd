package com.ekalips.cahscrowd.network

import com.ekalips.cahscrowd.data.user.remote.model.RemoteBaseUser
import com.ekalips.cahscrowd.data.user.remote.model.RemoteThisUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface Api {

    companion object {
        private const val TOKEN_FIELD = "Authorization"
    }

    @GET("user/{user_id)")
    fun getUser(@Header(TOKEN_FIELD) token: String, @Path("user_id") id: String): Call<RemoteBaseUser>

    @GET("me")
    fun getMe(@Header(TOKEN_FIELD) token: String): Call<RemoteThisUser>

}