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
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
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
    private val usersCache = ArrayList<BaseUser>()

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
        val userIds = HashSet<String>()
        events?.forEach {
            it.actions?.filter { it.user == null }?.forEach { userIds.add(it.userId) }
        }
        requiredUsers.postValue(userIds.toList())
    }

    private fun getUsersAndFillList(userIds: List<String>?) {
        userIds?.let {
            if (it.isNotEmpty()) {
                val finalList = userIds.toMutableList()
                val cachedUsers = ArrayList<BaseUser>(usersCache.size)

                it.forEach { id ->
                    val cachedUser = usersCache.find { it.id == id && it.loaded }
                    if (cachedUser != null) {
                        finalList.removeAll { it == id }
                        cachedUsers.add(cachedUser)
                    }
                }

                onUsersFetched(cachedUsers)

                if (finalList.isNotEmpty())
                    userDataProvider.getUsersSmart(*finalList.toTypedArray()).subscribe({ onUsersFetched(it) }, { it.printStackTrace() })
            }
        }
    }

    private fun onUsersFetched(users: List<BaseUser>) {
        if (users.isEmpty()) return

        synchronized(lock, {
            if (state.events.value != null) {
                Completable.fromAction {
                    synchronized(usersCache) {
                        val newUsers = users.toMutableList()
                        usersCache.forEach { chachedUser ->
                            newUsers.removeAll { it.id == chachedUser.id }
                        }
                        usersCache.addAll(newUsers)
                    }
                }.subscribe({}, { it.printStackTrace() })

                Observable.fromIterable(state.events.value!!)
                        .map { it.actions ?: ArrayList() }
                        .flatMapIterable { it }
                        .filter { it.user == null }
                        .map { action ->
                            users.find { it.id == action.userId }?.let { action.user = it }
                            action
                        }
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .toList()
                        .wrap()
                        .subscribe({ state.updateTrigger.postValue(null) }, { it.printStackTrace() })
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