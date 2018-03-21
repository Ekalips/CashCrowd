package com.ekalips.cahscrowd.stuff.paging.data_source

import android.arch.paging.PositionalDataSource
import android.util.Log
import io.objectbox.Box
import io.objectbox.query.Query
import io.objectbox.reactive.DataSubscription


open class BoxLimitOffsetDataSource<T> constructor(private val query: Query<T>) : PositionalDataSource<T>() {
    constructor(box: Box<T>) : this(box.query().build())


    private val dataSubscription: DataSubscription = query.subscribe().onlyChanges().observer {
        Log.d(javaClass.simpleName, "${it.count()}")
        invalidate()
    }

    /**
     * Count number of rows query can return
     */
    private fun countItems(): Int = query.count().toInt()

    override fun loadInitial(params: PositionalDataSource.LoadInitialParams,
                             callback: PositionalDataSource.LoadInitialCallback<T>) {
        val totalCount = countItems()
        if (totalCount == 0) {
            callback.onResult(emptyList(), 0, 0)
            return
        }

        // bound the size requested, based on known count
        val firstLoadPosition = PositionalDataSource.computeInitialLoadPosition(params, totalCount)
        val firstLoadSize = PositionalDataSource.computeInitialLoadSize(params, firstLoadPosition, totalCount)

        val list = loadRange(firstLoadPosition, firstLoadSize)
        if (list != null && list.size == firstLoadSize) {
            callback.onResult(list, firstLoadPosition, totalCount)
        } else {
            invalidate()
        }
    }

    override fun loadRange(params: PositionalDataSource.LoadRangeParams,
                           callback: PositionalDataSource.LoadRangeCallback<T>) {
        val list = loadRange(params.startPosition, params.loadSize)
        if (list != null) {
            callback.onResult(list)
        } else {
            invalidate()
        }
    }

    /**
     * Return the rows from startPos to startPos + loadCount
     */
    open fun loadRange(startPosition: Int, loadCount: Int): List<T>? {
        return query.find(startPosition.toLong(), loadCount.toLong())
    }
}