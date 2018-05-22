package com.ekalips.cahscrowd.deep

import android.net.Uri
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.event.EventsDataProvider
import com.ekalips.cahscrowd.data.user.UserDataProvider
import com.ekalips.cahscrowd.network.error.AuthenticationException
import com.ekalips.cahscrowd.providers.ResourceProvider
import com.ekalips.cahscrowd.stuff.base.CCViewModel
import com.ekalips.cahscrowd.stuff.navigation.Place
import com.ekalips.cahscrowd.stuff.utils.disposeBy
import javax.inject.Inject

class DeepLinkViewModel @Inject constructor(private val userDataProvider: UserDataProvider,
                                            private val eventsDataProvider: EventsDataProvider,
                                            private val resourceProvider: ResourceProvider) : CCViewModel<BaseViewState>() {
    override val state: BaseViewState = BaseViewState()

    //todo move deep link detection to some provider
    enum class DeepLinks(val path: String) {
        INVITE("/invite");

        companion object {
            fun forPath(path: String) = values().find { it.path == path }
        }
    }


    fun onDeepLink(action: String?, uri: Uri?) {
        uri?.let {
            handleDeepLink(it)
        }
    }

    private fun handleDeepLink(uri: Uri) {
        when (DeepLinks.forPath(uri.path)) {
            DeepLinks.INVITE -> handleInviteLink(uri)
        }
    }

    private fun handleInviteLink(uri: Uri) {
        val hash = uri.getQueryParameter("hash")
        if (!hash.isNullOrBlank()) {
            userDataProvider.checkUser()
                    .andThen(eventsDataProvider.acceptInvite(hash = hash))
                    .subscribe({
                        state.toast.postValue(resourceProvider.getString(R.string.success_accept_invite))
                        navigate(Place.MAIN)
                    }, {
                        handleError(it)
                        goBack()
                    })
                    .disposeBy(disposer)
        } else {
            deepLinkError()
        }
    }

    private fun deepLinkError() {
        state.toast.postValue(resourceProvider.getString(R.string.error_handle_deep_link))
        goBack()
    }

    private fun authError() {
        state.toast.postValue(resourceProvider.getString(R.string.error_auth_required))
        navigate(Place.AUTH)
        goBack()
    }

    override fun handleUncommonError(throwable: Throwable?) {
        when (throwable) {
            is AuthenticationException -> authError()
            else -> deepLinkError()
        }
    }
}