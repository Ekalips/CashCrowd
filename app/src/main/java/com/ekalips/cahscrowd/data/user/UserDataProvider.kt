package com.ekalips.cahscrowd.data.user

import com.ekalips.cahscrowd.data.user.local.LocalUserDataSource
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.data.user.model.ThisUser
import com.ekalips.cahscrowd.data.user.remote.RemoteUserDataSource
import com.ekalips.cahscrowd.stuff.ErrorHandler
import com.ekalips.cahscrowd.stuff.utils.wrap
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

    fun getAccessToken(): Single<String> {
        return localUserDataSource.getMyToken()
    }

    fun getUser(id: String): Observable<BaseUser> {
        return Observable.concatDelayError(Observable.just(localUserDataSource.getUser(id).toObservable(),
                getAccessToken().flatMap { remoteUserDataSource.getUser(it, id) }.toObservable()))
                .wrap(errorHandler.getHandler())
    }

    fun getMe(): Observable<ThisUser> {
        return Observable.concatDelayError(Observable.just(localUserDataSource.getMyUser().toObservable(),
                getAccessToken().flatMap { remoteUserDataSource.getMe(it) }.toObservable()))
                .wrap(errorHandler.getHandler())
    }

}
