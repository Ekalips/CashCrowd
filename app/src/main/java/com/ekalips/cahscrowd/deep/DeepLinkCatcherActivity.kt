package com.ekalips.cahscrowd.deep

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.ekalips.cahscrowd.stuff.base.CCActivity

class DeepLinkCatcherActivity : CCActivity<DeepLinkViewModel, ViewDataBinding>() {
    override val vmClass: Class<DeepLinkViewModel> = DeepLinkViewModel::class.java
    override val layoutId: Int = 0
    override val brRes: Int = 0
    override val inflate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (width, height) = resources.displayMetrics.widthPixels to resources.displayMetrics.heightPixels

        val parentView = FrameLayout(this)
        val loader = ProgressBar(this).also { it.isIndeterminate = true }
        parentView.addView(loader, FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER })
        setContentView(parentView, ViewGroup.LayoutParams(width, height))

        val action = intent.action
        val uri = intent.data
        viewModel.onDeepLink(action, uri)
    }
}