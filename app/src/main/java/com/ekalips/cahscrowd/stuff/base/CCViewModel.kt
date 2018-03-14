package com.ekalips.cahscrowd.stuff.base

import android.util.Log
import com.ekalips.base.state.BaseViewState
import com.ekalips.base.vm.BaseViewModel
import com.ekalips.base.vm.SingleLiveEvent
import com.ekalips.cahscrowd.stuff.InsignificantError
import com.ekalips.cahscrowd.stuff.navigation.Navigate
import com.ekalips.cahscrowd.stuff.navigation.Place


abstract class CCViewModel<out ViewState : BaseViewState> : BaseViewModel<ViewState>() {

    val navigationTrigger = SingleLiveEvent<Navigate>()
    protected val lock = Any()

    fun navigate(place: Place, payload: Any? = null) {
        navigationTrigger.postValue(Navigate(place, payload))
    }

    protected fun handleError(throwable: Throwable?) {
        when {
            throwable is InsignificantError -> Log.w(javaClass.simpleName, "handledCommonError: ${throwable.cause}")
            throwable != null -> handleUncommonError(throwable)
            else -> Log.w(javaClass.simpleName, "handledCommonError: received null throwable")
        }
    }

    open fun handleUncommonError(throwable: Throwable?) {
        throwable?.let {
            Log.e(javaClass.simpleName, "Unhandled error: $throwable")
            throw throwable
        }
    }

}