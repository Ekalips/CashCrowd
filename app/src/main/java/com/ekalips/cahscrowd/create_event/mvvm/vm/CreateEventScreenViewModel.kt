package com.ekalips.cahscrowd.create_event.mvvm.vm

import android.arch.lifecycle.MutableLiveData
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.create_event.mvvm.model.GuestUserWrap
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.utils.ContactUtils
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import javax.inject.Inject

class CreateEventScreenViewState : BaseViewState() {

    val eventTitle = MutableLiveData<String>()
    val eventDescription = MutableLiveData<String>()
    val guests = MutableLiveData<List<GuestUserWrap>>()

    val addGuestTrigger = SingleLiveEvent<Void>()

}

class CreateEventScreenViewModel @Inject constructor() : CCViewModel<CreateEventScreenViewState>() {
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

    fun createEvent(){
        //todo implement this
    }
}