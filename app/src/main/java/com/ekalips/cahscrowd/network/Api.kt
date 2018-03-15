package com.ekalips.cahscrowd.network

import com.ekalips.cahscrowd.data.user.remote.model.RemoteBaseUser
import com.ekalips.cahscrowd.data.user.remote.model.RemoteThisUser
import com.ekalips.cahscrowd.network.request.AuthBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    companion object {
        private const val TOKEN_FIELD = "Authorization"
    }

    @POST("auth")
    fun auth(@Body authBody: AuthBody): Call<RemoteThisUser>

    @GET("user/{user_id)")
    fun getUser(@Header(TOKEN_FIELD) token: String, @Path("user_id") id: String): Call<RemoteBaseUser>

    @GET("me")
    fun getMe(@Header(TOKEN_FIELD) token: String): Call<RemoteThisUser>

}