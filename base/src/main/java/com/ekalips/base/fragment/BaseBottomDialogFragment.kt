package com.ekalips.base.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekalips.base.providers.ToastProvider
import com.ekalips.base.state.BaseViewState
import com.ekalips.base.vm.BaseViewModel
import com.ekalips.base.vm.BaseViewModelFactory
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseBottomDialogFragment<VM : BaseViewModel<BaseViewState>,
        ParentVM : BaseViewModel<BaseViewState>,
        DataBinding : ViewDataBinding> : BottomSheetDialogFragment(), HasSupportFragmentInjector{


    lateinit var viewModel: VM
    lateinit var parentViewModel: ParentVM
    @Inject
    internal lateinit var vmFactory: BaseViewModelFactory

    @Inject
    internal lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>


    @Inject
    lateinit var toastProvider: ToastProvider

    abstract val vmClass: Class<VM>
    abstract val parentVMClass: Class<ParentVM>
    abstract val layoutId: Int
    abstract val brRes: Int

    var binding: DataBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        binding!!.setVariable(brRes, viewModel)
        binding!!.setLifecycleOwner(this)
        onBindingReady(binding!!)
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, vmFactory).get(vmClass)
        viewModel.state.toast.observe(this, Observer { it?.let { toastProvider.showToast(it) } })
        parentViewModel = ViewModelProviders.of(activity!!, vmFactory).get(parentVMClass)
    }

    open fun onBindingReady(binding: DataBinding) {

    }

    override fun supportFragmentInjector() = childFragmentInjector
}