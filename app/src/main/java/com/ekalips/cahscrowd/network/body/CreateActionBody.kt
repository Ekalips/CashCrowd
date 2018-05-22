package com.ekalips.cahscrowd.network.body

import com.squareup.moshi.Json

class CreateActionBody(@Json(name = "amount") private val amount: Double,
                       @Json(name = "type") private val type: Int = 2)