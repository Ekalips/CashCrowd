package com.ekalips.cahscrowd.main.mvvm.view.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.ekalips.cahscrowd.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.FragmentProfileBinding
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.child.ProfileFragmentViewModel
import com.ekalips.cahscrowd.stuff.base.CCFragment

class ProfileFragment : CCFragment<ProfileFragmentViewModel, MainScreenViewModel, FragmentProfileBinding>() {
    override val vmClass: Class<ProfileFragmentViewModel> = ProfileFragmentViewModel::class.java
    override val parentVMClass: Class<MainScreenViewModel> = MainScreenViewModel::class.java
    override val layoutId: Int = R.layout.fragment_profile
    override val brRes: Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.state.logOutConfirmTrigger.observe(this, Observer { showLogOutDialog() })
    }

    private fun showLogOutDialog() {
        context?.let { context ->
            AlertDialog.Builder(context)
                    .setTitle(R.string.dialog_logout_title)
                    .setMessage(R.string.dialog_logout_body)
                    .setPositiveButton(R.string.log_out, { _, _ -> viewModel.logOut(true) })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
        }
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}