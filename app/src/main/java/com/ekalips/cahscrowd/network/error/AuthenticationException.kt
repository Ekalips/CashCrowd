package com.ekalips.cahscrowd.network.error

sealed class AuthenticationException : RuntimeException("You are not authorised in the app")