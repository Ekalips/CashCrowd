package com.ekalips.cahscrowd.network

import com.ekalips.cahscrowd.data.action.remote.RemoteAction
import com.ekalips.cahscrowd.data.event.remote.RemoteEvent
import com.ekalips.cahscrowd.data.user.remote.model.RemoteBaseUser
import com.ekalips.cahscrowd.data.user.remote.model.RemoteThisUser
import com.ekalips.cahscrowd.network.body.CreateActionBody
import com.ekalips.cahscrowd.network.body.CreateEventBody
import com.ekalips.cahscrowd.network.request.AuthBody
import com.ekalips.cahscrowd.network.response.GetEventShareLinkResponse
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
    fun getMe(@Header(TOKEN_FIELD) token: String): Call<RemoteBaseUser>

    @GET("events")
    fun getEvents(@Header(TOKEN_FIELD) token: String): Call<List<RemoteEvent>>

    @POST("events")
    fun createEvent(@Header(TOKEN_FIELD) token: String, @Body body: CreateEventBody): Call<RemoteEvent>

    @POST("users")
    fun batchGetUsers(@Header(TOKEN_FIELD) token: String, @Body body: List<String>): Call<List<RemoteBaseUser>>

    @GET("events/{event_id}/actions")
    fun getEventActions(@Header(TOKEN_FIELD) token: String, @Path("event_id") id: String): Call<List<RemoteAction>>

    @POST("events/{event_id}/actions")
    fun createAction(@Header(TOKEN_FIELD) token: String, @Path("event_id") eventId: String, @Body body: CreateActionBody): Call<RemoteAction>

    @GET("events/{event_id}/participants")
    fun getEventParticipants(@Header(TOKEN_FIELD) token: String, @Path("event_id") eventId: String): Call<List<RemoteBaseUser>>

    @GET("events/{event_id}")
    fun getEvent(@Header(TOKEN_FIELD) token: String, @Path("event_id") eventId: String): Call<RemoteEvent>

    @POST("events/{event_id}/invite")
    fun getEventInviteLink(@Header(TOKEN_FIELD) token: String, @Path("event_id") eventId: String): Call<GetEventShareLinkResponse>

    @POST("invites")
    fun acceptInviteCode(@Header(TOKEN_FIELD) token: String, @Query("code") inviteCode: String): Call<RemoteEvent>

    @POST("invites")
    fun acceptInviteHash(@Header(TOKEN_FIELD) token: String, @Query("hash") inviteHash: String): Call<RemoteEvent>

}