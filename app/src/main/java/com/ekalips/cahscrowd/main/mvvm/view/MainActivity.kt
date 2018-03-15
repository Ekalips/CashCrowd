package com.ekalips.cahscrowd.main.mvvm.view

import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.ActivityMainBinding
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.stuff.base.CCActivity

class MainActivity : CCActivity<MainScreenViewModel, ActivityMainBinding>() {
    override val vmClass: Class<MainScreenViewModel> = MainScreenViewModel::class.java
    override val layoutId: Int = R.layout.activity_main
    override val brRes: Int = BR.vm
}
