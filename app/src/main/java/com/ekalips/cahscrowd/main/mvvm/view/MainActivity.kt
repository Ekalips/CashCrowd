package com.ekalips.cahscrowd.main.mvvm.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.ActivityMainBinding
import com.ekalips.cahscrowd.main.mvvm.model.EventsRecyclerViewAdapter
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.stuff.base.CCActivity

class MainActivity : CCActivity<MainScreenViewModel, ActivityMainBinding>() {
    override val vmClass: Class<MainScreenViewModel> = MainScreenViewModel::class.java
    override val layoutId: Int = R.layout.activity_main
    override val brRes: Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = EventsRecyclerViewAdapter()
        binding?.recyclerView?.adapter = adapter
        viewModel.state.events.observe(this, Observer { adapter.submitList(it) })
        viewModel.state.loading.observe(this, Observer { Log.d(javaClass.simpleName, "Loading: $it") })

        viewModel.state.refreshing.observe(this, Observer { binding?.swipeLay?.isRefreshing = it ?: false })
        binding?.swipeLay?.setOnRefreshListener { viewModel.refresh() }
    }
}
