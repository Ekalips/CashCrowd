package com.ekalips.cahscrowd.main.mvvm.view.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.databinding.FragmentEventsBinding
import com.ekalips.cahscrowd.main.mvvm.model.EventsRecyclerViewAdapter
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.child.EventsFragmentViewModel
import com.ekalips.cahscrowd.stuff.base.CCFragment
import com.ekalips.cahscrowd.stuff.navigation.Place

class EventsFragment : CCFragment<EventsFragmentViewModel, MainScreenViewModel, FragmentEventsBinding>() {
    override val vmClass: Class<EventsFragmentViewModel> = EventsFragmentViewModel::class.java
    override val parentVMClass: Class<MainScreenViewModel> = MainScreenViewModel::class.java
    override val layoutId: Int = R.layout.fragment_events
    override val brRes: Int = BR.vm

    private val adapter: EventsRecyclerViewAdapter
    private val adapterCallbacks = object : EventsRecyclerViewAdapter.AdapterCallbacks {
        override fun onEventClicked(event: Event) {
            parentViewModel.navigate(Place.EVENT, event.id)
        }
    }

    init {
        adapter = EventsRecyclerViewAdapter(adapterCallbacks)
    }

    private var appBarElevationThreshold: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appBarElevationThreshold = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics)
        viewModel.state.events.observe(this, Observer { adapter.submitList(it) })
        viewModel.state.addEventTrigger.observe(this, Observer { openAddEventDialog() })
    }

    override fun onBindingReady(binding: FragmentEventsBinding) {
        viewModel.state.loading.observe(this, Observer { binding.swipeLay.isRefreshing = it ?: false })
        viewModel.state.updateTrigger.observe(this, Observer { adapter.notifyItemRangeChanged(0, adapter.itemCount) })
        binding.eventsRecyclerView.adapter = adapter
        binding.swipeLay.setOnRefreshListener { viewModel.refresh() }
        setUpToolbarScrolling(binding)
        setUpCreateDialogCallback(null)
    }

    private var rvScrollY = 0F
    private fun setUpToolbarScrolling(binding: FragmentEventsBinding) {
        binding.appBar.isEnabled = false
        binding.eventsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                rvScrollY += dy
                binding.appBar.isEnabled = rvScrollY > appBarElevationThreshold
            }
        })
    }

    private fun openAddEventDialog() {
        val dialog = CreateEventDialogFragment.newInstance()
        setUpCreateDialogCallback(dialog)
        dialog.show(childFragmentManager, CreateEventDialogFragment.TAG)
    }

    private fun setUpCreateDialogCallback(dialog: CreateEventDialogFragment?) {
        if (dialog != null) {
            dialog.setCallback(fragmentCallback)
        } else {
            val fragment = childFragmentManager.findFragmentByTag(CreateEventDialogFragment.TAG) as CreateEventDialogFragment?
            fragment?.setCallback(fragmentCallback)
        }
    }

    private fun showFab(show: Boolean) {
        binding?.addFab?.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private val fragmentCallback = object : CreateEventDialogFragment.Companion.Callback {
        override fun onDialogOpen() {
            showFab(false)
        }

        override fun onDialogClose() {
            showFab(true)
        }

        override fun onCreateEventSelected() {
            viewModel.navigate(Place.CREATE_EVENT)
        }

        override fun onInviteSelected() {
            //todo implement this
        }
    }

    companion object {
        fun newInstance() = EventsFragment()
    }
}