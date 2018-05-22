package com.ekalips.cahscrowd.event.mvvm.vm.child

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import javax.inject.Inject

class EventParticipantsViewState : BaseViewState() {

    val eventId = MutableLiveData<String>()
    val participants = MediatorLiveData<List<BaseUser>>()
}

class EventParticipantsViewModel @Inject constructor(private val eventsDataProvider: EventsDataProvider) : CCViewModel<EventParticipantsViewState>() {
    override val state = EventParticipantsViewState()

    init {
        state.participants.addSource(state.eventId, { it?.let { fetchEventParticipants(it) } })
    }

    private fun fetchEventParticipants(eventId: String) {
        eventsDataProvider.getEventParticipants(eventId)
                .doOnSubscribe { state.loading.postValue(true) }
                .subscribe({
                    state.loading.postValue(false)
                    state.participants.postValue(it)
                }, { it.printStackTrace() })
    }
}