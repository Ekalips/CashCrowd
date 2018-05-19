package com.ekalips.cahscrowd.event.mvvm.vm

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import javax.inject.Inject

class EventScreenViewState(val eventId: MutableLiveData<String>,
                           val event: LiveData<Event>) : BaseViewState()

class EventScreenViewModel @Inject constructor(private val eventsDataProvider: EventsDataProvider) : CCViewModel<EventScreenViewState>() {

    override val state: EventScreenViewState

    init {
        val eventId = MutableLiveData<String>()
        val event = Transformations.switchMap(eventId, { eventsDataProvider.getEvent(it) })
        state = EventScreenViewState(eventId, event)
    }

    fun init(eventId: String) {
        state.eventId.value ?: state.eventId.postValue(eventId)
    }
}