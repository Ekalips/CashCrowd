package com.ekalips.cahscrowd.welcome.mvvm.vm

import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.navigation.Place
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(userDataProvider: UserDataProvider) : CCViewModel<BaseViewState>() {
    override val state: BaseViewState = BaseViewState()

    init {
        userDataProvider.getAccessToken().subscribe({
            checkTokenAndContinue(it)
        }, {
            checkTokenAndContinue(null)
        })
    }

    private fun checkTokenAndContinue(token: String?) {
        if (token.isNullOrBlank()) {
            navigate(Place.AUTH)
        } else {
            navigate(Place.MAIN)
        }
    }
}