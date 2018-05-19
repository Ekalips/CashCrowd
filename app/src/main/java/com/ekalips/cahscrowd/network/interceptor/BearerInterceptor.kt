package com.ekalips.cahscrowd.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class BearerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        chain.let {
            var currentToken = it.request().header("Authorization")
            if (currentToken != null) {
                currentToken = "Bearer $currentToken"
            }

            val newRequestBuilder = it.request().newBuilder()
            if (!currentToken.isNullOrBlank()) {
                newRequestBuilder.header("Authorization", currentToken!!)
            }
            return it.proceed(newRequestBuilder.build())
        }
    }

}