package com.ekalips.cahscrowd.event.mvvm.view


import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ekalips.base.stuff.getStatusBarHeight
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.ActivityEventBinding
import com.ekalips.cahscrowd.event.mvvm.model.EventActionsRecyclerView
import com.ekalips.cahscrowd.event.mvvm.vm.EventScreenViewModel
import com.ekalips.cahscrowd.stuff.base.CCActivity


class EventActivity : CCActivity<EventScreenViewModel, ActivityEventBinding>() {
    override val vmClass: Class<EventScreenViewModel> = EventScreenViewModel::class.java
    override val layoutId: Int = R.layout.activity_event
    override val brRes: Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding?.appBar?.setPadding(0, getStatusBarHeight(), 0, 0)
        binding?.tabLayout?.let {
            it.addTab(it.newTab().setText("Text 1"))
            it.addTab(it.newTab().setText("Text 2"))
            it.addTab(it.newTab().setText("Text 3"))
        }

        val adapter = EventActionsRecyclerView()
        viewModel.state.event.observe(this, Observer { adapter.submitList(it?.actions) })
        binding?.actionsRv?.adapter = adapter

        extractEventId(intent)?.let {
            viewModel.init(it)
        } ?: onEventNotFund()
    }

    private fun onEventNotFund() {
        toastProvider.showToast(R.string.error_event_not_found)
        finish()
    }

    companion object {
        private const val EXTRA_EVENT_ID = "eventId"

        fun extractEventId(intent: Intent?) = if (intent?.hasExtra(EXTRA_EVENT_ID) == true) intent.getStringExtra(EXTRA_EVENT_ID) else null

        fun getIntentFor(context: Context, eventId: String) = Intent(context, EventActivity::class.java)
                .also { it.putExtra(EXTRA_EVENT_ID, eventId) }
    }
}
