package com.ekalips.cahscrowd.main.mvvm.view.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.FragmentEventsBinding
import com.ekalips.cahscrowd.main.mvvm.model.EventsRecyclerViewAdapter
import com.ekalips.cahscrowd.main.mvvm.vm.child.EventFragmentViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.stuff.base.CCFragment

class EventsFragment : CCFragment<EventFragmentViewModel, MainScreenViewModel, FragmentEventsBinding>() {
    override val vmClass: Class<EventFragmentViewModel> = EventFragmentViewModel::class.java
    override val parentVMClass: Class<MainScreenViewModel> = MainScreenViewModel::class.java
    override val layoutId: Int = R.layout.fragment_events
    override val brRes: Int = BR.vm

    private val adapter = EventsRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.events.observe(this, Observer { adapter.submitList(it) })
        viewModel.state.loading.observe(this, Observer { Log.d(javaClass.simpleName, "Loading: $it") })
        viewModel.state.error.observe(this, Observer {
            Log.e(javaClass.simpleName, "error from get events: $it")
        })
    }

    override fun onBindingReady(binding: FragmentEventsBinding) {
        viewModel.state.refreshing.observe(this, Observer { binding.swipeLay.isRefreshing = it ?: false })
        binding.eventsRecyclerView.adapter = adapter
        binding.swipeLay.setOnRefreshListener { viewModel.refresh() }
    }


    companion object {
        fun newInstance() = EventsFragment()
    }
}