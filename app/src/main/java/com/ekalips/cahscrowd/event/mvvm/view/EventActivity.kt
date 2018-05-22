package com.ekalips.cahscrowd.event.mvvm.view


import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.View
import com.ekalips.base.stuff.TitledFragmentPagerAdapter
import com.ekalips.base.stuff.getStatusBarHeight
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.ActivityEventBinding
import com.ekalips.cahscrowd.event.mvvm.model.EventActionsRecyclerViewAdapter
import com.ekalips.cahscrowd.event.mvvm.view.child.EventActionsFragment
import com.ekalips.cahscrowd.event.mvvm.vm.EventScreenPages
import com.ekalips.cahscrowd.event.mvvm.vm.EventScreenViewModel
import com.ekalips.cahscrowd.stuff.base.CCActivity
import com.ekalips.cahscrowd.stuff.other.FabExpandingDialog


class EventActivity : CCActivity<EventScreenViewModel, ActivityEventBinding>() {
    override val vmClass: Class<EventScreenViewModel> = EventScreenViewModel::class.java
    override val layoutId: Int = R.layout.activity_event
    override val brRes: Int = BR.vm

    lateinit var viewPagerAdapter: TitledFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding?.appBar?.setPadding(0, getStatusBarHeight(), 0, 0)

        val adapter = EventActionsRecyclerViewAdapter()
        viewModel.state.event.observe(this, Observer { adapter.submitList(it?.actions) })
        viewModel.state.currentPage.observe(this, Observer { it?.let { onStatePageChangeListener(it) } })
        viewModel.state.addActionTrigger.observe(this, Observer { showAddActionDialog() })
        initViewPager()
        extractEventId(intent)?.let {
            viewModel.init(it)
        } ?: onEventNotFund()
    }

    private fun onEventNotFund() {
        toastProvider.showToast(R.string.error_event_not_found)
        finish()
    }

    private fun initViewPager() {
        viewPagerAdapter = TitledFragmentPagerAdapter(supportFragmentManager)

        for (page in viewModel.availablePages) {
            when (page) {
                EventScreenPages.ACTIONS -> viewPagerAdapter.addItem("Actions" to EventActionsFragment.newInstance())
            }
        }

        binding?.viewPager?.adapter = viewPagerAdapter
        binding?.viewPager?.addOnPageChangeListener(onViewPagerPageChangeListener)
        binding?.tabLayout?.setupWithViewPager(binding?.viewPager)

        viewModel.state.currentPage.value?.let { onStatePageChangeListener(it) }
                ?: viewModel.state.currentPage.postValue(viewModel.availablePages[0])
    }

    private val onViewPagerPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) = onViewPagerPageSelected(position)
        override fun onPageScrollStateChanged(state: Int) = Unit
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
    }

    private fun onViewPagerPageSelected(page: Int) {
        viewModel.availablePages.getOrNull(page)?.let { viewModel.state.currentPage.postValue(it) }
    }

    private fun onStatePageChangeListener(page: EventScreenPages) {
        val pageIndex = viewModel.availablePages.indexOfFirst { it == page }
        if (binding?.viewPager?.currentItem != pageIndex) {
            binding?.viewPager?.currentItem = pageIndex
        }
        when (page) {
            EventScreenPages.ACTIONS -> binding?.actionFab?.setImageResource(R.drawable.ic_attach_money)
        }
    }

    private fun showAddActionDialog() {
        val dp16 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)

        val dialogBuilder = FabExpandingDialog.Companion.FabExpandingDialogBuilder.create(this)
                .setTitle(R.string.add_action_title)
                .setFirstActionName(R.string.add_action_income_btn)
                .setSecondActionName(R.string.add_action_withdraw_btn)
                .setInitialFabBottomMargin(dp16)
                .setInitialFabEndMargin(dp16)

        val dialog = dialogBuilder.build()
        dialog.setCallback(addEventDialogCallback)
        dialog.show(supportFragmentManager, FabExpandingDialog.TAG)
    }

    private fun showFab(show: Boolean) {
        binding?.actionFab?.visibility = if (show) View.VISIBLE else View.GONE
    }

    private val addEventDialogCallback = object : FabExpandingDialog.Callback {
        override fun onDialogOpen() {
            showFab(false)
        }

        override fun onDialogClose() {
            showFab(true)
        }

        override fun onFirstOptionSelected() {

        }

        override fun onSecondOptionSelected() {

        }
    }

    companion object {
        private const val EXTRA_EVENT_ID = "eventId"

        fun extractEventId(intent: Intent?) = if (intent?.hasExtra(EXTRA_EVENT_ID) == true) intent.getStringExtra(EXTRA_EVENT_ID) else null

        fun getIntentFor(context: Context, eventId: String) = Intent(context, EventActivity::class.java)
                .also { it.putExtra(EXTRA_EVENT_ID, eventId) }
    }
}
