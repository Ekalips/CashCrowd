package com.ekalips.cahscrowd.stuff.base

import android.arch.lifecycle.Observer
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.ekalips.base.fragment.BaseBottomDialogFragment
import com.ekalips.base.state.BaseViewState
import com.ekalips.cahscrowd.providers.GlobalNavigationProvider
import com.ekalips.cahscrowd.stuff.navigation.Place
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class CCBottomDialogFragment<VM : CCViewModel<BaseViewState>, ParentVM : CCViewModel<BaseViewState>, DataBinding : ViewDataBinding> :
        BaseBottomDialogFragment<VM, ParentVM, DataBinding>() {

    @Inject
    lateinit var navigator: GlobalNavigationProvider

    protected val disposer: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigationTrigger.observe(this, Observer { it?.place?.let { place -> handleBaseNavigation(place, it.payload) } })
        viewModel.state.back.observe(this, Observer { dismiss() })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposer.dispose()
    }

    private fun handleBaseNavigation(place: Place, payload: Any?) {
        if (payload == null && context != null) {
            parentViewModel.navigate(place, payload)
        } else {
            handleNavigation(place, payload)
        }
    }

    open fun handleNavigation(place: Place, payload: Any?) {

    }
}