package com.ekalips.cahscrowd.stuff.navigation

enum class Place {
    SPLASH, AUTH, MAIN
}


data class Navigate(val place: Place, val payload: Any? = null)