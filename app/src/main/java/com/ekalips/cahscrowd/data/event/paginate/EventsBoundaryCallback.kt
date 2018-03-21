package com.ekalips.cahscrowd.data.event.paginate

import android.arch.paging.PagedList
import android.support.annotation.MainThread
import android.util.Log
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.stuff.paging.PagingRequestHelper
import com.ekalips.cahscrowd.stuff.paging.createStatusLiveData
import com.ekalips.cahscrowd.stuff.utils.wrap
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Creates new BoundaryCallback for Events store
 *
 * @param remoteFetcher This must execute async function to fetch new events from network. `String?` is value for "lastEventId", `List<Event>` value for events response
 * @param save This must save values to local store
 */
class EventsBoundaryCallback(private val remoteFetcher: ((String?) -> Observable<List<Event>>),
                             private val save: ((List<Event>) -> Unit)) : PagedList.BoundaryCallback<Event>() {

    val helper = PagingRequestHelper(::retry)
    val networkState = helper.createStatusLiveData()

    @MainThread
    override fun onZeroItemsLoaded() {
        Log.d("PAGING", "onZeroItemsLoaded")
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL, {
            remoteFetcher(null).subscribe({ result ->
                saveItems(result, it)
            }, { throwable -> it.recordFailure(throwable) })
        })
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Event) {
        Log.d("PAGING", "onItemAtEndLoaded $itemAtEnd")
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER, {
            remoteFetcher(itemAtEnd.id).subscribe({ result ->
                saveItems(result, it)
            }, { throwable -> it.recordFailure(throwable) })
        })
    }

    private fun saveItems(data: List<Event>, it: PagingRequestHelper.Request.Callback) {
        save(data)
        it.recordSuccess()
    }

    @MainThread
    override fun onItemAtFrontLoaded(itemAtFront: Event) {
        Log.d("PAGING", "onItemAtFrontLoaded $itemAtFront")
    }

    private fun retry(runnable: Runnable) {
        Completable.fromRunnable(runnable).wrap().subscribe()
    }
}