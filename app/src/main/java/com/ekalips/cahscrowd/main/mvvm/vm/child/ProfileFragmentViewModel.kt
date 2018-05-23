package com.ekalips.cahscrowd.main.mvvm.vm.child

import android.arch.lifecycle.MediatorLiveData
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import javax.inject.Inject

class ProfileViewState : BaseViewState() {
    val user = MediatorLiveData<BaseUser>() // I think we don't need info that contains in "ThisUser" (token, deviceToken)

    val logOutConfirmTrigger = SingleLiveEvent<Void>()
}

class ProfileFragmentViewModel @Inject constructor(private val userDataProvider: UserDataProvider) : CCViewModel<ProfileViewState>() {
    override val state = ProfileViewState()

    init {
        state.user.addSource(userDataProvider.getMeLiveData(), { state.user.postValue(it) })
    }

    fun logOut(confirmed: Boolean) {
        if (!confirmed) {
            state.logOutConfirmTrigger.call()
        } else {
            logOut()
        }
    }

    private fun logOut() {

    }
}