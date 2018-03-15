package com.ekalips.cahscrowd.main.mvvm.vm

import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import javax.inject.Inject

class MainScreenViewState: BaseViewState()

class MainScreenViewModel @Inject constructor(): CCViewModel<MainScreenViewState>(){
    override val state: MainScreenViewState = MainScreenViewState()
}