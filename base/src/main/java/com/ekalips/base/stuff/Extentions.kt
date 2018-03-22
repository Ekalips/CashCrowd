package com.ekalips.base.stuff

inline fun Boolean?.ifThenElse(action: () -> Any): Any? {
    return if (this == true) {
        action()
        Any()
    } else null
}

inline fun Boolean?.ifThen(action: () -> Any) {
    if (this == true) {
        action()
    }
}