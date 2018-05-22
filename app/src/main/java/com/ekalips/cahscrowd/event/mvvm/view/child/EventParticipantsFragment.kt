package com.ekalips.cahscrowd.event.mvvm.view.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.FragmentEventParticipantsBinding
import com.ekalips.cahscrowd.event.mvvm.model.EventParticipantsRecyclerViewAdapter
import com.ekalips.cahscrowd.event.mvvm.vm.EventScreenViewModel
import com.ekalips.cahscrowd.event.mvvm.vm.child.EventParticipantsViewModel
import com.ekalips.cahscrowd.stuff.base.CCFragment

class EventParticipantsFragment : CCFragment<EventParticipantsViewModel, EventScreenViewModel, FragmentEventParticipantsBinding>() {
    override val vmClass: Class<EventParticipantsViewModel> = EventParticipantsViewModel::class.java
    override val parentVMClass: Class<EventScreenViewModel> = EventScreenViewModel::class.java
    override val layoutId: Int = R.layout.fragment_event_participants
    override val brRes: Int = BR.vm

    private val adapter = EventParticipantsRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentViewModel.state.eventId.observe(this, Observer { viewModel.state.eventId.postValue(it) })
        viewModel.state.participants.observe(this, Observer { adapter.submitList(it) })
    }

    override fun onBindingReady(binding: FragmentEventParticipantsBinding) {
        binding.participantsRv.adapter = adapter
    }

    companion object {
        fun newInstance() = EventParticipantsFragment()
    }

}