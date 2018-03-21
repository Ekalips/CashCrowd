package com.ekalips.cahscrowd.data.event.paginate

import com.ekalips.cahscrowd.data.action.local.LocalAction
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import com.ekalips.cahscrowd.stuff.paging.data_source.BoxLimitOffsetDataSource
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.reactive.DataSubscriptionList

class EventsPaginateDataSource(eventBox: Box<Event>,
                               boxStore: BoxStore) : BoxLimitOffsetDataSource<Event>(eventBox) {

    private val relationsSub = boxStore.subscribe().onlyChanges().weak()
            .dataSubscriptionList(DataSubscriptionList().apply {
                add(boxStore.subscribe(LocalBaseUser::class.java).onlyChanges().weak().observer { })
                add(boxStore.subscribe(LocalAction::class.java).onlyChanges().weak().observer { })
            })
            .observer { invalidate() }

}