package com.ekalips.cahscrowd.data.user.remote

import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.data.user.model.ThisUser
import com.ekalips.cahscrowd.network.Api
import com.ekalips.cahscrowd.network.body.GetUsersBody
import com.ekalips.cahscrowd.network.request.AuthBody
import com.ekalips.cahscrowd.stuff.ServerError
import io.reactivex.Single
import javax.inject.Inject

class RemoteUserDataSource @Inject constructor(private val api: Api) {


    fun getUser(token: String, id: String): Single<BaseUser> {
        return Single.fromCallable {
            val response = api.getUser(token, id).execute()
            if (response.isSuccessful) {
                return@fromCallable response.body()!!
            }
            throw ServerError(response.code())
        }
    }

    fun getUsers(token: String, vararg id: String): Single<List<BaseUser>> {
        return Single.fromCallable {
            val response = api.batchGetUsers(token, GetUsersBody(id.asList())).execute()
            if (response.isSuccessful) {
                return@fromCallable response.body()!!
            }
            throw ServerError(response.code())
        }
    }

    fun getMe(token: String): Single<BaseUser> {
        return Single.fromCallable {
            val response = api.getMe(token).execute()
            if (response.isSuccessful) {
                return@fromCallable response.body()!!
            }
            throw ServerError(response.code())
        }
    }

    fun authenticate(idToken: String, deviceToken: String?): Single<ThisUser> {
        return Single.fromCallable {
            val response = api.auth(AuthBody(idToken, deviceToken)).execute()
            if (response.isSuccessful) {
                return@fromCallable response.body()!!
            }
            throw ServerError(response.code())
        }
    }

}