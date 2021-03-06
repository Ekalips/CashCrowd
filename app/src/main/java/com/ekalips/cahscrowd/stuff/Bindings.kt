package com.ekalips.cahscrowd.stuff

import android.content.res.ColorStateList
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("tint")
fun setTint(imageView: ImageView, tint: Int?) {
    if (tint == null || tint == 0) {
        imageView.imageTintList = null
    } else {
        imageView.imageTintList = ColorStateList.valueOf(tint)
    }
}

@BindingAdapter("backgroundTint")
fun setBackgroundTint(view: View, tint: Int?) {
    if (tint == null || tint == 0) {
        view.backgroundTintList = null
    } else {
        view.backgroundTintList = ColorStateList.valueOf(tint)
    }
}

@BindingAdapter("alpha")
fun setAlpha(view: View, alpha: Float?) {
    view.alpha = alpha ?: 1F
}

@BindingAdapter("src")
fun setImage(imageView: ImageView, url: String?) {
    Glide.with(imageView.context.applicationContext).load(url).apply(RequestOptions.circleCropTransform()).into(imageView)
}

@BindingAdapter("src", "placeholder")
fun setImageWithPlaceholder(imageView: ImageView, url: String?, placeholder: Drawable?) {
    Glide.with(imageView.context.applicationContext).load(url).apply(RequestOptions.circleCropTransform().placeholder(placeholder)).into(imageView)
}

@BindingAdapter("src", "text")
fun setImageWithText(imageView: ImageView, url: String?, text: String?) {
    val originText = text ?: ""
    val placeholderTextArray = originText.split(" ".toRegex()).take(2).map { it.getOrElse(0, { ' ' }) }.toCharArray()
    val placeholderText = String(placeholderTextArray)


    val placeholder = TextDrawable.builder()
            .buildRound(placeholderText, ColorGenerator.MATERIAL.getColor(originText))

    Glide.with(imageView.context.applicationContext).load(url)
            .apply(RequestOptions.circleCropTransform().placeholder(placeholder))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
}

@BindingAdapter("android:layout_marginBottom")
fun setMarginBottom(view: View, margin: Float?) {
    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        (view.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = margin?.toInt() ?: 0
    }
}

@BindingAdapter("android:layout_marginTop")
fun setMarginTop(view: View, margin: Float?) {
    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        (view.layoutParams as ViewGroup.MarginLayoutParams).topMargin = margin?.toInt() ?: 0
    }
}

@BindingAdapter("android:layout_marginBottom", "android:layout_marginTop")
fun setVerticalMargin(view: View, marginBot: Float?, marginTop: Float?) {
    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        (view.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = marginBot?.toInt() ?: 0
        (view.layoutParams as ViewGroup.MarginLayoutParams).topMargin = marginTop?.toInt() ?: 0
    }
}

@BindingAdapter("android:onClick")
fun setClickRunnable(view: View, click: Runnable?) {
    view.setOnClickListener(if (click != null) View.OnClickListener { click.run() } else null)
}