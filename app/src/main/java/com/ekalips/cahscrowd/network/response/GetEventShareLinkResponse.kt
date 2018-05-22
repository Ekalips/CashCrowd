package com.ekalips.cahscrowd.network.response

import com.squareup.moshi.Json

class GetEventShareLinkResponse(@Json(name = "code") val code: String,
                                @Json(name = "url") val url: String)