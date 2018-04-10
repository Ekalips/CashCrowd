package com.ekalips.cahscrowd.data.user.local

import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import com.ekalips.cahscrowd.data.user.local.model.LocalThisUser
import com.ekalips.cahscrowd.data.user.local.model.toLocal
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.data.user.model.ThisUser
import com.ekalips.cahscrowd.providers.SharedPreferencesProvider
import io.reactivex.Single
import javax.inject.Inject

class LocalUserDataSource @Inject constructor(private val userDao: LocalUserDao,
                                              sharedPreferencesProvider: SharedPreferencesProvider) {

    private val userSharedPrefs = sharedPreferencesProvider.getNamedPreferences(USER_PREFS_NAME)


    fun getMyToken(): Single<String> = Single.fromCallable { userSharedPrefs.getString(PREF_USER_ACCESS_TOKEN, "") }

    fun getMyUser(): Single<ThisUser> {
        return Single.fromCallable {
            val userId = userSharedPrefs.getString(PREF_USER_ID, "")
            val userToken = userSharedPrefs.getString(PREF_USER_ACCESS_TOKEN, "")
            val deviceToken = userSharedPrefs.getString(PREF_USER_DEVICE_TOKEN, "")
            return@fromCallable Triple(userId, userToken, deviceToken)
        }.flatMap({ (id, token, deviceToken) -> getUser(id).map { LocalThisUser(id, it.name, it.avatar, token, deviceToken, it.loaded) } })
    }

    fun getUser(userId: String): Single<BaseUser> {
        return Single.fromCallable {
            userDao.getUser(userId) ?: LocalBaseUser("", "", null, false)
        }
    }

    fun saveUser(user: BaseUser) {
        userDao.insertUsers(user.toLocal())
    }

    fun saveMyUser(user: ThisUser) {
        saveUser(user)
        userSharedPrefs.edit().apply {
            putString(PREF_USER_ID, user.id)
            putString(PREF_USER_ACCESS_TOKEN, user.accessToken)
            putString(PREF_USER_DEVICE_TOKEN, user.deviceToken)
        }.apply()
    }

    fun getAll(): Single<List<BaseUser>> = Single.fromCallable { box.all }

    fun saveUsers(vararg users: BaseUser) {
        users.forEach { saveUser(it) }
    }

    companion object {
        private const val USER_PREFS_NAME = "com.ekalips.cahscrowd.USER_DATA"

        private const val PREF_USER_ID = "id"
        private const val PREF_USER_ACCESS_TOKEN = "access_token"
        private const val PREF_USER_DEVICE_TOKEN = "device_token"

    }

}