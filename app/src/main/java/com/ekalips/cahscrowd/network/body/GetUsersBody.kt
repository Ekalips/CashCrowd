package com.ekalips.cahscrowd.network.body

import com.squareup.moshi.Json

class GetUsersBody(@Json(name = "users") val users: List<String>)