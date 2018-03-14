package com.ekalips.cahscrowd.stuff.base

import android.arch.lifecycle.Observer
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.ekalips.base.activity.BaseActivity
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.providers.GlobalNavigationProvider
import com.ekalips.cahscrowd.stuff.navigation.Place
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by Ekalips on 2/7/18.
 */
abstract class CCActivity<VM : CCViewModel<BaseViewState>, DataBinding : ViewDataBinding> : BaseActivity<VM, DataBinding>() {

    @Inject
    lateinit var navigator: GlobalNavigationProvider

    protected val disposer: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigationTrigger.observe(this, Observer { it?.place?.let { place -> handleBaseNavigation(place, it.payload) } })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposer.dispose()
    }

    private fun handleBaseNavigation(place: Place, payload: Any?) {
        if (payload == null) {
            when (place) {
                Place.SPLASH -> navigator navigateToSplashScreen this
                Place.AUTH -> navigator navigateToAuthScreen this
                else -> handleNavigation(place, payload)
            }
        } else {
            handleNavigation(place, payload)
        }
    }

    open fun handleNavigation(place: Place, payload: Any?) {

    }
}
