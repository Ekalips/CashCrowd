package com.ekalips.cahscrowd.data.user.remote

import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.data.user.model.ThisUser
import com.ekalips.cahscrowd.data.user.remote.model.RemoteBaseUser
import com.ekalips.cahscrowd.data.user.remote.model.RemoteThisUser
import com.ekalips.cahscrowd.network.Api
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
            if (2==2){
                return@fromCallable getMockUser(id)
            }
            throw ServerError(response.code())
        }
    }

    fun getMe(token: String): Single<ThisUser> {
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
            if (2 == 2) { // todo Remove this when api is done
                return@fromCallable getMockMyUser()
            }
            throw ServerError(response.code())
        }
    }

    private fun getMockMyUser() = RemoteThisUser("token", "device_token", getMockUser())
    private fun getMockUser(id: String = "iddd") = RemoteBaseUser(id, "Test User Name", null)

}