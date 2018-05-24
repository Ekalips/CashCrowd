package com.ekalips.cahscrowd.data.user

import android.arch.lifecycle.LiveData
import com.ekalips.cahscrowd.data.user.local.LocalUserDataSource
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.data.user.model.ThisUser
import com.ekalips.cahscrowd.data.user.remote.RemoteUserDataSource
import com.ekalips.cahscrowd.stuff.ErrorHandler
import com.ekalips.cahscrowd.stuff.utils.wrap
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataProvider @Inject constructor(private val localUserDataSource: LocalUserDataSource,
                                           private val remoteUserDataSource: RemoteUserDataSource,
                                           private val errorHandler: ErrorHandler) {

    fun authenticate(idToken: String, deviceToken: String?): Single<ThisUser> {
        return remoteUserDataSource.authenticate(idToken, deviceToken).map { it.also { localUserDataSource.saveMyUser(it) } }.wrap(errorHandler.getHandler())
    }

    fun getAllLiveData() = localUserDataSource.getAllLiveData()

    fun getAllFlowable(): Flowable<List<LocalBaseUser>> = localUserDataSource.getAllUsersFlowable()

    fun getAccessToken(): Single<String> {
        return localUserDataSource.getMyToken()
    }

    fun checkUser(): Completable {
        return localUserDataSource.getMyToken().flatMapCompletable { token ->
            Completable.create {
                if (it.isDisposed) return@create
                if (token.isBlank()) {
                    it.onError(RuntimeException("Authentication error"))
                } else {
                    it.onComplete()
                }
            }
        }
    }

    fun getUser(id: String): Observable<BaseUser> {
        return Observable.concatDelayError(Observable.just(localUserDataSource.getUser(id).toObservable(),
                getAccessToken().flatMap { remoteUserDataSource.getUser(it, id) }.toObservable()))
                .wrap(errorHandler.getHandler())
    }

    fun getUserLiveData(id: String): LiveData<BaseUser> {
        getAccessToken().flatMap { remoteUserDataSource.getUser(it, id) }
                .wrap(errorHandler.getHandler())
                .subscribe({ saveUsers(it) }, { it.printStackTrace() })
        return localUserDataSource.getUserLiveData(id)
    }

    fun getMeLiveData(): LiveData<ThisUser> {
        getAccessToken().flatMap { remoteUserDataSource.getMe(it) }
                .doOnSuccess { localUserDataSource.saveUser(it) }
                .wrap(errorHandler.getHandler())
                .subscribe({ }, { it.printStackTrace() })
        return localUserDataSource.getMyUserLiveData()
    }

    fun saveUsers(vararg users: BaseUser) {
        Completable.fromAction { localUserDataSource.saveUsers(*users) }
                .wrap(errorHandler.getHandler())
                .subscribe({ "${users.size} users saved" }, { println("Error saving user"); it.printStackTrace() })

    }

    fun loadUsers(vararg ids: String): Completable {
        return getAccessToken().flatMap { remoteUserDataSource.getUsers(it, *ids) }
                .doOnSuccess { localUserDataSource.saveUsers(*it.toTypedArray()) }
                .wrap(errorHandler.getHandler())
                .toCompletable()
    }

}
