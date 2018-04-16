package com.ekalips.cahscrowd.main.navigation

import android.support.v4.app.FragmentManager
import com.ekalips.base.navigation.BaseFragmentNavigator
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.di.scopes.ActivityScope
import com.ekalips.cahscrowd.main.mvvm.view.child.EventsFragment
import javax.inject.Inject

@ActivityScope
class MainActivityNavigator @Inject constructor(fragmentManager: FragmentManager) : BaseFragmentNavigator(fragmentManager) {

    override val containerId: Int = R.id.fragment_container
    private var currentScreen: MainActivityPlace? = null

    fun handleNavigation(place: MainActivityPlace) {
        when (place) {
            MainActivityPlace.EVENTS -> navigateToEventsScreen()
            MainActivityPlace.ACCOUNTING -> navigateToAccountingScreen()
            MainActivityPlace.PROFILE -> navigateToProfileScreen()
        }
    }

    private fun navigateToEventsScreen() {
        if (compareWithCurrentAndApply(MainActivityPlace.EVENTS)) {
            val newFragment = getFragment("EventsFragment") ?: EventsFragment.newInstance()
            replaceFragment(newFragment, "EventsFragment")
        }
    }

    private fun navigateToAccountingScreen() {
        if (compareWithCurrentAndApply(MainActivityPlace.ACCOUNTING)) {
            clearCurrentFragment()
        }
    }

    private fun navigateToProfileScreen() {
        if (compareWithCurrentAndApply(MainActivityPlace.PROFILE)) {
            clearCurrentFragment()
        }
    }

    private fun compareWithCurrentAndApply(newPlace: MainActivityPlace): Boolean {
        if (currentScreen != newPlace) {
            currentScreen = newPlace
            return true
        } else {
            return false
        }
    }

}


enum class MainActivityPlace {
    EVENTS, ACCOUNTING, PROFILE
}