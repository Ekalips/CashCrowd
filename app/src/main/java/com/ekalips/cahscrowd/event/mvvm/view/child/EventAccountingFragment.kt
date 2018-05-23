package com.ekalips.cahscrowd.event.mvvm.view.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.FragmentEventAccountingBinding
import com.ekalips.cahscrowd.event.mvvm.vm.EventScreenViewModel
import com.ekalips.cahscrowd.event.mvvm.vm.child.EventAccountingViewModel
import com.ekalips.cahscrowd.stuff.base.CCFragment

class EventAccountingFragment : CCFragment<EventAccountingViewModel, EventScreenViewModel, FragmentEventAccountingBinding>() {
    override val vmClass: Class<EventAccountingViewModel> = EventAccountingViewModel::class.java
    override val parentVMClass: Class<EventScreenViewModel> = EventScreenViewModel::class.java
    override val layoutId: Int = R.layout.fragment_event_accounting
    override val brRes: Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentViewModel.state.eventId.observe(this, Observer { viewModel.state.eventId.postValue(it) })
        parentViewModel.state.refreshStatisticsTrigger.observe(this, Observer { viewModel.refresh() })
    }

    companion object {
        fun newInstance() = EventAccountingFragment()
    }
}