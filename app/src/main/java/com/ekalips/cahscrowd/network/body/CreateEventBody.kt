package com.ekalips.cahscrowd.network.body

import com.squareup.moshi.Json

class CreateEventBody(@Json(name = "title") private val title: String,
                      @Json(name = "description") private val description: String? = null)