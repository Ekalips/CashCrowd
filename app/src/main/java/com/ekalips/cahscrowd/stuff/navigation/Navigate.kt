package com.ekalips.cahscrowd.stuff.navigation

enum class Place{
    SPLASH, MAIN, MODULES
}


data class Navigate(val place: Place, val payload: Any?)