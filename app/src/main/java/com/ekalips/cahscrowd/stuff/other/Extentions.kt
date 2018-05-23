package com.ekalips.cahscrowd.stuff.other

import android.arch.lifecycle.MutableLiveData

infix fun <T> MutableLiveData<T>.post(value: T){
    this.postValue(value)
}