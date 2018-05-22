package com.ekalips.cahscrowd.stuff.views

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.ekalips.base.stuff.views.CircleOutlineProvider

class SimpleCicleImageView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        outlineProvider = CircleOutlineProvider()
        clipToOutline = true
    }
}