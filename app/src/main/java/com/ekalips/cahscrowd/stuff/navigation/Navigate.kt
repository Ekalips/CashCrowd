package com.ekalips.cahscrowd.stuff.navigation

enum class Place {
    SPLASH, AUTH, MAIN, CREATE_EVENT, EVENT
}


data class Navigate(val place: Place, val payload: Any? = null)