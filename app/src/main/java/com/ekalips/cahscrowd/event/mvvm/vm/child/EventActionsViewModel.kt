package com.ekalips.cahscrowd.event.mvvm.vm.child

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.action.ActionsDataProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import javax.inject.Inject

class EventActionsViewState(val eventId: MutableLiveData<String>,
                            val actions: LiveData<List<Action>>) : BaseViewState()

class EventActionsViewModel @Inject constructor(private val actionsDataProvider: ActionsDataProvider) : CCViewModel<EventActionsViewState>() {
    override val state: EventActionsViewState

    init {
        val eventId = MutableLiveData<String>()
        val actions = Transformations.switchMap(eventId, { actionsDataProvider.getActionsForEvent(it) })
        state = EventActionsViewState(eventId, actions)
    }

    fun setEventId(eventId: String) {
        state.eventId.postValue(eventId)
    }
}