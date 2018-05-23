package com.ekalips.cahscrowd.create_event.mvvm.vm

import android.arch.lifecycle.MutableLiveData
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.create_event.mvvm.model.GuestUserWrap
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.providers.ResourceProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.utils.ContactUtils
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import javax.inject.Inject

class CreateEventScreenViewState : BaseViewState() {

    val eventTitle = MutableLiveData<String>()
    val eventDescription = MutableLiveData<String>()
    val guests = MutableLiveData<List<GuestUserWrap>>()
    val createInProgres = MutableLiveData<Boolean>()

    val addGuestTrigger = SingleLiveEvent<Void>()
}

class CreateEventScreenViewModel @Inject constructor(private val resourceProvider: ResourceProvider,
                                                     private val eventsDataProvider: EventsDataProvider) : CCViewModel<CreateEventScreenViewState>() {
    override val state = CreateEventScreenViewState()


    fun onAddGuestClicked() {
        state.addGuestTrigger.call()
    }

    fun onContactAdded(contact: ContactUtils.Contact) {
        if (contact.email.isNullOrBlank()) return

        synchronized(lock) {
            var curGuests = state.guests.value ?: ArrayList()
            val isAlreadyAdded = curGuests.find { contact.email == it.email } != null
            if (!isAlreadyAdded) {
                val newData = curGuests.toMutableList()
                newData.add(GuestUserWrap("-", contact.name, "-", true, contact.email!!))
                curGuests = newData
            }
            state.guests.postValue(curGuests)
        }
    }

    fun onContactRemoved(guest: GuestUserWrap) {
        synchronized(lock) {
            val curGuests = state.guests.value ?: ArrayList()
            if (curGuests.find { guest.email == it.email } != null) {
                val newData = curGuests.toMutableList()
                newData.remove(guest)
                state.guests.postValue(newData)
            }
        }
    }

    fun createEvent() {
        synchronized(lock) {
            if (state.createInProgres.value == true || state.eventTitle.value.isNullOrBlank() || state.eventDescription.value.isNullOrBlank()) {
                return
            }
        }
        eventsDataProvider.createEvent(state.eventTitle.value!!, state.eventDescription.value!!)
                .doOnSubscribe { state.createInProgres.postValue(true) }
                .doOnTerminate { state.createInProgres.postValue(false) }
                .subscribe({
                    state.toast.postValue(resourceProvider.getString(R.string.success_event_create))
                    goBack()
                }, {
                    it.printStackTrace()
                    state.toast.postValue(resourceProvider.getString(R.string.error_event_create))
                })
    }
}