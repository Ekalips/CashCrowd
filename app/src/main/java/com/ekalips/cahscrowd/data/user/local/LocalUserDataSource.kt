package com.ekalips.cahscrowd.data.user.local

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import com.ekalips.cahscrowd.data.db.CashDB
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import com.ekalips.cahscrowd.data.user.local.model.LocalThisUser
import com.ekalips.cahscrowd.data.user.local.model.toLocal
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.data.user.model.ThisUser
import com.ekalips.cahscrowd.providers.SharedPreferencesProvider
import com.ekalips.cahscrowd.stuff.utils.wrap
import io.reactivex.Single
import javax.inject.Inject

class LocalUserDataSource @Inject constructor(private val cashDB: CashDB,
                                              private val userDao: LocalUserDao,
                                              sharedPreferencesProvider: SharedPreferencesProvider) {

    private val userSharedPrefs = sharedPreferencesProvider.getNamedPreferences(USER_PREFS_NAME)


    fun getMyToken(): Single<String> = Single.fromCallable { userSharedPrefs.getString(PREF_USER_ACCESS_TOKEN, "") }

    fun getMyUser(): Single<ThisUser> {
        return getUserSpecifics()
                .flatMap({ (id, token, deviceToken) -> getUser(id).map { LocalThisUser(id, it.name, it.avatar, token, deviceToken, it.loaded) } })
    }

    fun getMyUserLiveData(): LiveData<ThisUser> {
        val myUserSpecData = MediatorLiveData<Triple<String, String, String>>()
        val userData = Transformations.switchMap(myUserSpecData, { (id) -> getUserLiveData(id) })
        val myUser = MediatorLiveData<ThisUser>()
        myUser.addSource(userData, {
            if (it != null && myUserSpecData.value != null) {
                val (_, token, deviceToken) = myUserSpecData.value!!
                myUser.postValue(LocalThisUser(it.id, it.name, it.avatar, token, deviceToken, it.loaded))
            }
        })
        getUserSpecifics().wrap().subscribe({ myUserSpecData.postValue(it) }, { it.printStackTrace() })
        return myUser
    }

    private fun getUserSpecifics(): Single<Triple<String, String, String>> {
        return Single.fromCallable {
            val userId = userSharedPrefs.getString(PREF_USER_ID, "")
            val userToken = userSharedPrefs.getString(PREF_USER_ACCESS_TOKEN, "")
            val deviceToken = userSharedPrefs.getString(PREF_USER_DEVICE_TOKEN, "")
            return@fromCallable Triple(userId, userToken, deviceToken)
        }
    }

    fun getUser(userId: String): Single<BaseUser> {
        return Single.fromCallable {
            userDao.getUser(userId) ?: LocalBaseUser("", "", null, false)
        }
    }

    fun getUserLiveData(userId: String): LiveData<BaseUser> {
        return userDao.getUserLiveData(userId) as LiveData<BaseUser>
    }

    fun saveUser(user: BaseUser) {
        cashDB.runInTransaction {
            userDao.insert(user.toLocal())
        }
    }

    fun saveMyUser(user: ThisUser) {
        saveUser(user)
        userSharedPrefs.edit().apply {
            putString(PREF_USER_ID, user.id)
            putString(PREF_USER_ACCESS_TOKEN, user.accessToken)
            putString(PREF_USER_DEVICE_TOKEN, user.deviceToken)
        }.apply()
    }

    fun getAll(): Single<List<BaseUser>> = Single.fromCallable { userDao.getAllUsers() }

    fun saveUsers(vararg users: BaseUser) {
        cashDB.runInTransaction {
            userDao.insert(*users.map { it.toLocal() }.toTypedArray())
        }
    }

    companion object {
        private const val USER_PREFS_NAME = "com.ekalips.cahscrowd.USER_DATA"

        private const val PREF_USER_ID = "id"
        private const val PREF_USER_ACCESS_TOKEN = "access_token"
        private const val PREF_USER_DEVICE_TOKEN = "device_token"

    }

}