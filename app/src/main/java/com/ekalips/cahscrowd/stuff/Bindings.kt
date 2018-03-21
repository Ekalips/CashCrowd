package com.ekalips.cahscrowd.stuff

import android.content.res.ColorStateList
import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView

@BindingAdapter("tint")
fun setTint(imageView: ImageView, tint: Int?) {
    if (tint == null) {
        imageView.imageTintList = null
    } else {
        imageView.imageTintList = ColorStateList.valueOf(tint)
    }
}

@BindingAdapter("alpha")
fun setAlpha(view: View, alpha: Float?) {
    view.alpha = alpha ?: 1F
}