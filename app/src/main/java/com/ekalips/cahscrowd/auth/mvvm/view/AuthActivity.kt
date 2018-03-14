package com.ekalips.cahscrowd.auth.mvvm.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.android.databinding.library.baseAdapters.BR
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.auth.mvvm.vm.AuthScreenViewModel
import com.ekalips.cahscrowd.databinding.ActivityAuthBinding
import com.ekalips.cahscrowd.stuff.base.CCActivity
import com.firebase.ui.auth.AuthUI


class AuthActivity : CCActivity<AuthScreenViewModel, ActivityAuthBinding>() {

    companion object {
        private const val REQUEST_GOOGLE_SIGN_IN = 0
    }

    override val vmClass: Class<AuthScreenViewModel> = AuthScreenViewModel::class.java
    override val layoutId: Int = R.layout.activity_auth
    override val brRes: Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.signInTrigger.observe(this, Observer { startSignIn() })
    }

    private fun startSignIn() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .build(), REQUEST_GOOGLE_SIGN_IN)
    }
}
