package com.ekalips.cahscrowd.network.request

import com.squareup.moshi.Json

class AuthBody(@Json(name = "token") val idToken: String,
               @Json(name = "deviceToken") val deviceToken: String?)