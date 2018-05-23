package com.ekalips.cahscrowd.event.mvvm.vm

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.action.ActionsDataProvider
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.providers.ResourceProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.navigation.Place
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import javax.inject.Inject

enum class EventScreenPages {
    ACTIONS, PARTICIPANTS, ACCOUNTING
}

class EventScreenViewState(val eventId: MutableLiveData<String>,
                           val event: LiveData<Event>) : BaseViewState() {
    val currentPage = MutableLiveData<EventScreenPages>()

    val addActionTrigger = SingleLiveEvent<Void>()
    val refreshStatisticsTrigger = SingleLiveEvent<Void>()
}

class EventScreenViewModel @Inject constructor(private val eventsDataProvider: EventsDataProvider,
                                               private val actionsDataProvider: ActionsDataProvider,
                                               private val resourceProvider: ResourceProvider) : CCViewModel<EventScreenViewState>() {

    override val state: EventScreenViewState

    val availablePages = EventScreenPages.values()

    init {
        val eventId = MutableLiveData<String>()
        val event = Transformations.switchMap(eventId, { eventsDataProvider.getEvent(it) })
        state = EventScreenViewState(eventId, event)
    }

    fun init(eventId: String) {
        state.eventId.postValue(eventId)
    }

    fun executePageAction() {
        when (state.currentPage.value) {
            EventScreenPages.ACTIONS -> state.addActionTrigger.call()
            EventScreenPages.PARTICIPANTS -> shareEvent()
            EventScreenPages.ACCOUNTING -> state.refreshStatisticsTrigger.call()
            else -> println(state.currentPage.value)
        }
    }

    private fun shareEvent() {
        state.eventId.value?.let {
            eventsDataProvider.getEventShareLink(it).subscribe({
                navigate(Place.SHARE, resourceProvider.getString(R.string.event_share_string, it.second, it.first))
            }, { it.printStackTrace() })
        }
    }

    fun createAction(value: String?, negative: Boolean): Boolean {
        val amount = value?.toDoubleOrNull()
        return if (amount != null) {
            createAction(amount * if (negative) -1 else 1)
            true
        } else {
            false
        }
    }

    private fun createAction(amount: Double) {
        val eventId = state.eventId.value
        eventId?.let {
            actionsDataProvider.createAction(eventId, amount)
                    .subscribe({ state.toast.postValue(resourceProvider.getString(R.string.success_create_action)) },
                            { state.toast.postValue(resourceProvider.getString(R.string.error_create_action)) })
        }
    }
}