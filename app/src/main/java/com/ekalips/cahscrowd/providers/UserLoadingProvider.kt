package com.ekalips.cahscrowd.providers

import android.text.format.DateUtils
import android.util.Log
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.data.user.model.BaseUser
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserLoadingProvider @Inject constructor(private val userDataProvider: UserDataProvider) {

    private val userLoadErrorsLog = HashMap<String, Long?>()
    private val lock = Any()

    fun start() {
        userDataProvider.getAllFlowable()
                .subscribeOn(Schedulers.io())
                .subscribe({ onUsersUpdateTriggered(it) }, { it.printStackTrace() })
    }

    private fun onUsersUpdateTriggered(users: List<BaseUser>?) {
        users?.let {
            val unLoadedUsers = it.filter { !it.loaded }.map { it.id }.filter {
                val errorTimeStamp = userLoadErrorsLog[it]
                errorTimeStamp == null || ((System.currentTimeMillis() - errorTimeStamp) < RETRY_AFTER_ERROR_TIME)
            }

            if (unLoadedUsers.isEmpty())
                return

            userDataProvider.loadUsers(*unLoadedUsers.toTypedArray())
                    .subscribe({
                        synchronized(lock) {
                            unLoadedUsers.forEach { userLoadErrorsLog[it] = null }
                        }
                        Log.d(TAG, "Successfully loaded ${unLoadedUsers.size} users")
                    }, {
                        synchronized(lock) {
                            unLoadedUsers.forEach { userLoadErrorsLog[it] = System.currentTimeMillis() }
                        }
                        Log.e(TAG, "Error loading ${unLoadedUsers.size} users", it)
                    })
        }
    }

    companion object {
        private const val TAG = "UserLoadingProvider"
        private const val RETRY_AFTER_ERROR_TIME = DateUtils.MINUTE_IN_MILLIS * 1
    }

}