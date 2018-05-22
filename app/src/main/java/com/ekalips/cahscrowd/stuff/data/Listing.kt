package com.ekalips.cahscrowd.stuff.data

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.ekalips.cahscrowd.stuff.paging.NetworkState

data class Listing<T>(
        // the LiveData of paged lists for the UI to observe
        val data: LiveData<List<T>>,
        // represents the network request status to show to the user
        val networkState: LiveData<NetworkState>,
        // refreshes the whole data and fetches it from scratch.
        val refresh: () -> Unit,
        // retries any failed requests.
        val retry: () -> Unit)