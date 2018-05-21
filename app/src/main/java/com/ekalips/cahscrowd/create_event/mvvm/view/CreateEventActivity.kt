package com.ekalips.cahscrowd.create_event.mvvm.view

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.TypedValue
import com.ekalips.base.stuff.getStatusBarHeight
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.create_event.mvvm.model.GuestUserWrap
import com.ekalips.cahscrowd.create_event.mvvm.model.GuestsRecyclerViewAdapter
import com.ekalips.cahscrowd.create_event.mvvm.vm.CreateEventScreenViewModel
import com.ekalips.cahscrowd.databinding.ActivityCreateEventBinding
import com.ekalips.cahscrowd.stuff.base.CCActivity
import com.ekalips.cahscrowd.stuff.utils.ContactUtils
import com.example.base.BR
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class CreateEventActivity : CCActivity<CreateEventScreenViewModel, ActivityCreateEventBinding>() {
    override val vmClass: Class<CreateEventScreenViewModel> = CreateEventScreenViewModel::class.java
    override val layoutId: Int = R.layout.activity_create_event
    override val brRes: Int = BR.vm


    companion object {
        private const val PICK_CONTACT = 1
        fun getIntentFor(context: Context) = Intent(context, CreateEventActivity::class.java)
    }

    private var appBarElevationThreshold: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = GuestsRecyclerViewAdapter(adapterCallbacks)

        binding?.let {
            appBarElevationThreshold = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
            setUpToolbarScrolling(it)
            setSupportActionBar(it.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            it.appBar.setPadding(0, getStatusBarHeight(), 0, 0)
            it.guestsRv.adapter = adapter
        }

        viewModel.state.addGuestTrigger.observe(this, Observer<Void> { showContactPickerWithPermissionCheck() })
        viewModel.state.guests.observe(this, Observer { adapter.submitList(it) })
    }

    private val adapterCallbacks = object : GuestsRecyclerViewAdapter.Companion.AdapterCallbacks {
        override fun onRemoveClick(guest: GuestUserWrap) {
            viewModel.onContactRemoved(guest)
        }
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    fun showContactPicker() {
        val i = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Email.CONTENT_URI)
        startActivityForResult(i, PICK_CONTACT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            val contact = ContactUtils.resolveContactFromPicker(this, data)
            contact?.let { viewModel.onContactAdded(it) }
        }
    }

    private var rvScrollY = 0F
    private fun setUpToolbarScrolling(binding: ActivityCreateEventBinding) {
        binding.appBar.isEnabled = false
        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            rvScrollY += (oldScrollY - scrollY)
            binding.appBar.isEnabled = rvScrollY > appBarElevationThreshold
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
