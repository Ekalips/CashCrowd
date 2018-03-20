package com.ekalips.cahscrowd.data.event.paginate

import com.ekalips.cahscrowd.data.action.Action
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.stuff.paging.data_source.BoxLimitOffsetDataSource
import io.objectbox.Box

class EventsPaginateDataSource(eventBox: Box<Event>,
                               actionsBox: Box<Action>) : BoxLimitOffsetDataSource<Event>(eventBox) {

    private val actionsDataSub = actionsBox.query().build().subscribe().onlyChanges()
            .observer { invalidate() }

}