package com.ekalips.cahscrowd.welcome.mvvm.view

import android.databinding.ViewDataBinding
import com.ekalips.cahscrowd.stuff.base.CCActivity
import com.ekalips.cahscrowd.welcome.mvvm.vm.SplashScreenViewModel

class SplashActivity : CCActivity<SplashScreenViewModel, ViewDataBinding>() {
    override val vmClass: Class<SplashScreenViewModel> = SplashScreenViewModel::class.java
    override val layoutId: Int = 0
    override val brRes: Int = 0
    override val inflate: Boolean = false
}
