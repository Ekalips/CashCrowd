package com.ekalips.cahscrowd.main.mvvm.vm

import android.arch.lifecycle.MediatorLiveData
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.main.navigation.MainActivityPlace
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import javax.inject.Inject

class MainScreenViewState : BaseViewState() {

    val navigation = MediatorLiveData<MainActivityPlace>()

}

class MainScreenViewModel @Inject constructor() : CCViewModel<MainScreenViewState>() {
    override val state = MainScreenViewState()

    init {
        changeScreen(MainActivityPlace.EVENTS)
    }

    fun changeScreen(mainActivityPlace: MainActivityPlace) {
        state.navigation.value = mainActivityPlace
    }
}