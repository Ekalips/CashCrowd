package com.ekalips.cahscrowd.main.mvvm.vm.child

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.paging.NetworkState
import com.ekalips.cahscrowd.stuff.paging.Status
import com.ekalips.cahscrowd.stuff.utils.disposeBy
import com.ekalips.cahscrowd.stuff.utils.wrap
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import io.reactivex.Observable
import javax.inject.Inject

class EventsFragmentViewState : BaseViewState() {

    val events = MediatorLiveData<List<Event>>()
    val error = MediatorLiveData<String>()

    val addEventTrigger = SingleLiveEvent<Void>()
    val updateTrigger = SingleLiveEvent<Void>()

}

class EventsFragmentViewModel @Inject constructor(private val eventsDataProvider: EventsDataProvider,
                                                  private val userDataProvider: UserDataProvider) : CCViewModel<EventsFragmentViewState>() {
    override val state: EventsFragmentViewState = EventsFragmentViewState()

    private val listing = eventsDataProvider.getEventsListing()

    private val requiredUsers = MutableLiveData<List<String>>()
    private val eventsObserver = Observer<List<Event>> { checkForEventUsers(it) }
    private val fetchUsersObserver = Observer<List<String>> { getUsersAndFillList(it) }

    init {
        state.events.addSource(listing.data, { state.events.postValue(it) })
        state.loading.addSource(listing.networkState, { state.loading.postValue(it == NetworkState.LOADING) })
        state.error.addSource(listing.networkState, {
            if (it?.status == Status.FAILED) {
                state.error.postValue(it.msg)
            }
        })

        state.events.observeForever(eventsObserver)
        requiredUsers.observeForever(fetchUsersObserver)
    }

    private fun checkForEventUsers(events: List<Event>?) {
        val userIds = ArrayList<String>()
        events?.forEach {
            it.actions?.filter { it.user == null }?.forEach { userIds.add(it.userId) }
        }
        requiredUsers.postValue(userIds)
    }

    private fun getUsersAndFillList(userIds: List<String>?) {
        userIds?.let {
            if (it.isNotEmpty())
                userDataProvider.getUsersSmart(*it.toTypedArray()).subscribe({ onUsersFetched(it) }, { it.printStackTrace() })
        }
    }

    private fun onUsersFetched(users: List<BaseUser>) {
        synchronized(lock, {
            if (state.events.value != null) {
                Observable.fromIterable(state.events.value!!)
                        .map { it.actions ?: ArrayList() }
                        .flatMapIterable { it }
                        .filter { it.user == null }
                        .map { action ->
                            users.find { it.id == action.userId }?.let { action.user = it }
                            action
                        }
                        .toList()
                        .wrap()
                        .subscribe({ state.updateTrigger.call() }, { it.printStackTrace() })
                        .disposeBy(disposer)
            }
        })
    }

    fun refresh() {
        listing.refresh()
    }

    fun onAddClick() {
        state.addEventTrigger.call()
    }

    override fun onCleared() {
        super.onCleared()
        state.events.removeObserver(eventsObserver)
        requiredUsers.removeObserver(fetchUsersObserver)
    }
}