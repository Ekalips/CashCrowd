package com.ekalips.cahscrowd.stuff

import com.ekalips.base.providers.ToastProvider
import com.ekalips.cahscrowd.R
import com.squareup.moshi.JsonDataException
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.exceptions.UndeliverableException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor(private val toastProvider: ToastProvider) {

    fun handleError(throwable: Throwable): Boolean {
        throwable.printStackTrace()

        val errror = when (throwable) {
            is OnErrorNotImplementedException -> throwable.cause
            is UndeliverableException -> throwable.cause
            else -> throwable
        }

        return when (errror) {
            is IOException -> {
                when (errror) {
                    is ConnectException -> toastProvider.showToast(R.string.unable_to_communicate_to_server)
                    is SocketTimeoutException -> toastProvider.showToast(R.string.unable_to_communicate_to_server)
                    is SocketException -> toastProvider.showToast(R.string.unable_to_communicate_to_server)
                    !is InterruptedIOException -> toastProvider.showToast(R.string.no_internet_connection)
                }
                true
            }
            is JsonDataException -> {
                toastProvider.showToast(R.string.malformed_json)
                true
            }
            is ServerError -> {
                return when {
                    errror.code >= 500 -> {
                        toastProvider.showToast(R.string.server_error)
                        true
                    }
                    else -> false
                }
            }
            else -> false
        }
    }

    fun getHandler(): (Throwable) -> (Boolean) {
        return { handleError(it) }
    }

}


class InsignificantError : RuntimeException {
    constructor(cause: Throwable) : super(cause)
    constructor() : super()
}

class ServerError(val code: Int) : RuntimeException("Unexpected server error with code: $code")