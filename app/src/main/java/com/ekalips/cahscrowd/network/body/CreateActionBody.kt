package com.ekalips.cahscrowd.network.body

import com.squareup.moshi.Json

class CreateActionBody(@Json(name = "value") private val amount: Double)