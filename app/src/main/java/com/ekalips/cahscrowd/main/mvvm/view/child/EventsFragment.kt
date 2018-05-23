package com.ekalips.cahscrowd.main.mvvm.view.child

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.ekalips.base.stuff.getBottomNavBarHeight
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.event.Event
import com.ekalips.cahscrowd.databinding.DialogEnterInviteCodeBinding
import com.ekalips.cahscrowd.databinding.FragmentEventsBinding
import com.ekalips.cahscrowd.main.mvvm.model.EventsRecyclerViewAdapter
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.child.EventsFragmentViewModel
import com.ekalips.cahscrowd.stuff.base.CCFragment
import com.ekalips.cahscrowd.stuff.navigation.Place
import com.ekalips.cahscrowd.stuff.other.FabExpandingDialog
import com.ekalips.cahscrowd.stuff.utils.disposeBy
import com.ekalips.cahscrowd.stuff.utils.wrap
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

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

    private lateinit var eventEmitter: ObservableEmitter<List<Event>>
    private val eventsObservable: Observable<List<Event>> = PublishSubject.create {
        eventEmitter = it
    }
    private val uiHandler = Handler()

    init {
        adapter = EventsRecyclerViewAdapter(adapterCallbacks)
        eventsObservable
                .wrap()
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe {
                    uiHandler.post { adapter.submitList(it) }
                }.disposeBy(disposer)
    }

    private var appBarElevationThreshold: Float = 0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appBarElevationThreshold = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics)
        viewModel.state.events.observe(this, Observer {
            it?.let { eventEmitter.onNext(it) }
        })
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
        val endMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
        val bottomMargin = endMargin + context.getBottomNavBarHeight() + endMargin / 2
        val dialogBuilder = FabExpandingDialog.Companion.FabExpandingDialogBuilder.create(context!!)
                .setTitle(R.string.create_event_title)
                .setFirstActionName(R.string.create_event_btn_text)
                .setSecondActionName(R.string.join_event_btn_text)
                .setInitialFabEndMargin(endMargin)
                .setInitialFabBottomMargin(bottomMargin)
                .setFirstActionImg(R.drawable.ic_add)
                .setSecondActionImg(R.drawable.ic_person_add)
        val dialog = dialogBuilder.build()
        setUpCreateDialogCallback(dialog)
        dialog.show(childFragmentManager, FabExpandingDialog.TAG)
    }

    private fun setUpCreateDialogCallback(dialog: FabExpandingDialog?) {
        if (dialog != null) {
            dialog.setCallback(fragmentCallback)
        } else {
            val fragment = childFragmentManager.findFragmentByTag(FabExpandingDialog.TAG) as FabExpandingDialog?
            fragment?.setCallback(fragmentCallback)
        }
    }

    private fun showFab(show: Boolean) {
        binding?.addFab?.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showEnterInviteCodeDialog() {
        context?.let {
            val binding = DataBindingUtil.inflate<DialogEnterInviteCodeBinding>(LayoutInflater.from(it), R.layout.dialog_enter_invite_code, null, false)
            val dialog = AlertDialog.Builder(it)
                    .setView(binding.root)
                    .show()

            val code = MediatorLiveData<String>()
            binding.code = code
            binding.onAccept = Runnable {
                if (viewModel.acceptInviteCode(code.value))
                    dialog.dismiss()
            }
            binding.onCancel = Runnable { dialog.dismiss() }

        }
    }

    private val fragmentCallback = object : FabExpandingDialog.Callback {

        override fun onDialogOpen() {
            showFab(false)
        }

        override fun onDialogClose() {
            showFab(true)
        }

        override fun onFirstOptionSelected() {
            viewModel.navigate(Place.CREATE_EVENT)
        }

        override fun onSecondOptionSelected() {
            showEnterInviteCodeDialog()
        }
    }

    companion object {
        fun newInstance() = EventsFragment()
    }
}