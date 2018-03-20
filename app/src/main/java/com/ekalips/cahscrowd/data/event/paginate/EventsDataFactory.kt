package com.ekalips.cahscrowd.data.event.paginate

import android.arch.paging.DataSource
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.event.local.LocalEvent
import com.ekalips.cahscrowd.stuff.paging.LimitOffsetDataSource
import io.objectbox.Box


class EventsDataSourceFactory(private val box: Box<LocalEvent>) : DataSource.Factory<Int, Event> {
    override fun create(): DataSource<Int, Event> {
        return LimitOffsetDataSource(box as Box<Event>)
    }
}