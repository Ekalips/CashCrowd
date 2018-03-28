package com.ekalips.cahscrowd.main.mvvm.view.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.FragmentEventsBinding
import com.ekalips.cahscrowd.main.mvvm.model.EventsRecyclerViewAdapter
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.child.EventFragmentViewModel
import com.ekalips.cahscrowd.stuff.base.CCFragment

class EventsFragment : CCFragment<EventFragmentViewModel, MainScreenViewModel, FragmentEventsBinding>() {
    override val vmClass: Class<EventFragmentViewModel> = EventFragmentViewModel::class.java
    override val parentVMClass: Class<MainScreenViewModel> = MainScreenViewModel::class.java
    override val layoutId: Int = R.layout.fragment_events
    override val brRes: Int = BR.vm

    private val adapter = EventsRecyclerViewAdapter()

    private var appBarElevationThreshold: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appBarElevationThreshold = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics)
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
        setUpToolbarScrolling(binding)
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

    private fun getRvScrollAmount(): Int {
        val pos = IntArray(2, { 0 })
        binding?.swipeLay?.getLocationInWindow(pos)
        return pos[1] - (binding?.swipeLay?.top ?: 0)
    }


    companion object {
        fun newInstance() = EventsFragment()
    }
}