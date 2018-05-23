package com.ekalips.cahscrowd.network.body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CreateActionBody(@Json(name = "amount") val amount: Double,
                       @Json(name = "type") val type: Int = 2)