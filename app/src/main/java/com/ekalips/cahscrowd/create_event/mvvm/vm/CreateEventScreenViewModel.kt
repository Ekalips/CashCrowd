package com.ekalips.cahscrowd.create_event.mvvm.vm

import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import javax.inject.Inject

class CreateEventScreenViewState : BaseViewState()

class CreateEventScreenViewModel @Inject constructor(): CCViewModel<CreateEventScreenViewState>() {
    override val state = CreateEventScreenViewState()

    init {

    }
}