package com.ekalips.cahscrowd.event.mvvm.vm.child

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.data.statistics.StatisticData
import com.ekalips.cahscrowd.providers.ResourceProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.other.post
import javax.inject.Inject

class EventAccountingViewState : BaseViewState() {

    val eventId = MutableLiveData<String>()
    val statistic = MediatorLiveData<StatisticData>()

}

class EventAccountingViewModel @Inject constructor(private val eventsDataProvider: EventsDataProvider,
                                                   private val resourceProvider: ResourceProvider) : CCViewModel<EventAccountingViewState>() {
    override val state = EventAccountingViewState()

    init {
        state.statistic.addSource(state.eventId, { it?.let { requestStatisticForEvent(it) } })
    }

    private fun requestStatisticForEvent(eventId: String) {
        eventsDataProvider.getEventStatistics(eventId)
                .doOnSubscribe { state.loading.postValue(true) }
                .doAfterTerminate { state.loading.postValue(false) }
                .subscribe({ state.statistic.postValue(it) }, { handleError(it) })
    }

    fun refresh() {
        state.eventId.value?.let { requestStatisticForEvent(it) }
    }

    override fun handleUncommonError(throwable: Throwable?) {
        state.toast post resourceProvider.getString(R.string.error_fetching_event_accounting)
    }
}