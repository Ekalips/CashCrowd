package com.ekalips.cahscrowd.welcome.mvvm.vm

import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.navigation.Place
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor() : CCViewModel<BaseViewState>() {
    override val state: BaseViewState = BaseViewState()

    init {
        navigate(Place.AUTH)
    }
}