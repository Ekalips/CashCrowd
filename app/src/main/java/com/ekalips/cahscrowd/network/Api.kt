package com.ekalips.cahscrowd.network

import com.ekalips.cahscrowd.data.action.remote.RemoteAction
import com.ekalips.cahscrowd.data.event.remote.RemoteEvent
import com.ekalips.cahscrowd.data.user.remote.model.RemoteBaseUser
import com.ekalips.cahscrowd.data.user.remote.model.RemoteThisUser
import com.ekalips.cahscrowd.network.body.CreateEventBody
import com.ekalips.cahscrowd.network.request.AuthBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    companion object {
        private const val TOKEN_FIELD = "Authorization"
    }

    @POST("auth")
    fun auth(@Body authBody: AuthBody): Call<RemoteThisUser>

    @GET("user/{user_id}")
    fun getUser(@Header(TOKEN_FIELD) token: String, @Path("user_id") id: String): Call<RemoteBaseUser>

    @GET("me")
    fun getMe(@Header(TOKEN_FIELD) token: String): Call<RemoteThisUser>

    @GET("events")
    fun getEvents(@Header(TOKEN_FIELD) token: String): Call<List<RemoteEvent>>

    @POST("events")
    fun createEvent(@Header(TOKEN_FIELD) token: String, @Body body: CreateEventBody): Call<RemoteEvent>

    @POST("users")
    fun batchGetUsers(@Header(TOKEN_FIELD) token: String, @Body body: List<String>): Call<List<RemoteBaseUser>>

    @GET("events/{event_id}/actions")
    fun getEventActions(@Header(TOKEN_FIELD) token: String, @Path("event_id") id: String): Call<List<RemoteAction>>

}