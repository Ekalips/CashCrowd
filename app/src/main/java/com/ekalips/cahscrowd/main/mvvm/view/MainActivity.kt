package com.ekalips.cahscrowd.main.mvvm.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.ActivityMainBinding
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.main.navigation.MainActivityNavigator
import com.ekalips.cahscrowd.main.navigation.MainActivityPlace
import com.ekalips.cahscrowd.stuff.base.CCActivity
import javax.inject.Inject

class MainActivity : CCActivity<MainScreenViewModel, ActivityMainBinding>() {
    override val vmClass: Class<MainScreenViewModel> = MainScreenViewModel::class.java
    override val layoutId: Int = R.layout.activity_main
    override val brRes: Int = BR.vm

    lateinit var localNavigator: MainActivityNavigator
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.state.navigation.observe(this, Observer { it?.let { localNavigator.handleNavigation(it) } })
        binding?.bottomNavigation?.setOnNavigationItemSelectedListener(bottomNavigationListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.bottomNavigation?.setOnNavigationItemReselectedListener(null)
    }

    private val bottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_dashboard -> viewModel.changeScreen(MainActivityPlace.EVENTS)
            R.id.menu_profile -> viewModel.changeScreen(MainActivityPlace.PROFILE)
            R.id.menu_stat -> viewModel.changeScreen(MainActivityPlace.ACCOUNTING)
            else -> {
            }
        }
        return@OnNavigationItemSelectedListener true
    }
}