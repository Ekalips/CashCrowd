package com.ekalips.cahscrowd.event.mvvm.view.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.FragmentEventActionsBinding
import com.ekalips.cahscrowd.event.mvvm.model.EventActionsRecyclerViewAdapter
import com.ekalips.cahscrowd.event.mvvm.vm.EventScreenViewModel
import com.ekalips.cahscrowd.event.mvvm.vm.child.EventActionsViewModel
import com.ekalips.cahscrowd.stuff.base.CCFragment

class EventActionsFragment : CCFragment<EventActionsViewModel, EventScreenViewModel, FragmentEventActionsBinding>() {
    override val vmClass: Class<EventActionsViewModel> = EventActionsViewModel::class.java
    override val parentVMClass: Class<EventScreenViewModel> = EventScreenViewModel::class.java
    override val layoutId: Int = R.layout.fragment_event_actions
    override val brRes: Int = BR.vm

    private val adapter = EventActionsRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel.state.eventId.observe(this, Observer { it?.let { viewModel.setEventId(it) } })
        viewModel.state.actions.observe(this, Observer { adapter.submitList(it) })
    }

    override fun onBindingReady(binding: FragmentEventActionsBinding) {
        binding.actionsRv.adapter = adapter
    }

    companion object {
        fun newInstance() = EventActionsFragment()
    }
}