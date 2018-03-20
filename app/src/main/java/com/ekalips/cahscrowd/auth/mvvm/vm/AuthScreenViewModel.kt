package com.ekalips.cahscrowd.auth.mvvm.vm

import android.util.Log
import com.ekalips.base.state.BaseViewState
import com.ekalips.base.vm.SingleLiveEvent
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.providers.ResourceProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.navigation.Place
import com.ekalips.cahscrowd.stuff.utils.disposeBy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject

class AuthScreenState : BaseViewState() {
    val signInTrigger = SingleLiveEvent<Void>()
}

class AuthScreenViewModel @Inject constructor(private val userDataProvider: UserDataProvider,
                                              private val resourceProvider: ResourceProvider) : CCViewModel<AuthScreenState>() {
    override val state: AuthScreenState = AuthScreenState()
    private val authStateListener = FirebaseAuth.AuthStateListener { checkUserAndProceed(it.currentUser) }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }

    fun signIn() {
        state.signInTrigger.call()
    }


    private fun checkUserAndProceed(user: FirebaseUser?) {
        user?.let {
            user.getIdToken(true).addOnCompleteListener {
                val idToken = it.result.token ?: ""
                userDataProvider.authenticate(idToken, FirebaseInstanceId.getInstance().token)
                        .doOnSubscribe { state.loading.postValue(true) }
                        .doFinally { state.loading.postValue(false) }
                        .subscribe({ navigate(Place.MAIN) }, { handleError(it) })
                        .disposeBy(disposer)
            }
        }
    }

    override fun handleUncommonError(throwable: Throwable?) {
        Log.e(javaClass.simpleName, "handleCommonError", throwable)
        state.toast.postValue(resourceProvider.getString(R.string.error_auth))
    }
}