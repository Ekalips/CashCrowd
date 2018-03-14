package com.ekalips.cahscrowd.auth.mvvm.vm

import com.ekalips.base.state.BaseViewState
import com.ekalips.base.vm.SingleLiveEvent
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthScreenState : BaseViewState() {
    val signInTrigger = SingleLiveEvent<Void>()
}

class AuthScreenViewModel @Inject constructor(private val userDataProvider: UserDataProvider) : CCViewModel<AuthScreenState>() {
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

        }
    }


}