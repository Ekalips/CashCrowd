package com.ekalips.cahscrowd.event.mvvm.vm.child

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.ActionsDataProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import javax.inject.Inject

class EventActionsViewState : BaseViewState() {
    val eventId = MutableLiveData<String>()
    val actions = MediatorLiveData<List<Action>>()
}

class EventActionsViewModel @Inject constructor(private val actionsDataProvider: ActionsDataProvider) : CCViewModel<EventActionsViewState>() {
    override val state = EventActionsViewState()

    init {
        state.actions.addSource(state.eventId, { it?.let { fetchActionsForEvent(it) } })
    }

    private fun fetchActionsForEvent(eventId: String) {
        actionsDataProvider.getActionsForEvent(eventId).subscribe({
            state.actions.postValue(it)
        }, { it.printStackTrace() })
    }

    fun setEventId(eventId: String) {
        state.eventId.postValue(eventId)
    }
}