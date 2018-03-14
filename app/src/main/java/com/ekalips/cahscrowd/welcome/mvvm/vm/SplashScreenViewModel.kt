package com.ekalips.cahscrowd.welcome.mvvm.vm

import com.ekalips.cahscrowd.stuff.base.GHViewModel
import com.ekalips.base.state.BaseViewState
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor() : GHViewModel<BaseViewState>() {
    override val state: BaseViewState = BaseViewState()
}