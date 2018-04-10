package com.ekalips.cahscrowd.create_event.mvvm.view

import android.content.Context
import android.content.Intent
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.create_event.mvvm.vm.CreateEventScreenViewModel
import com.ekalips.cahscrowd.databinding.ActivityCreateEventBinding
import com.ekalips.cahscrowd.stuff.base.CCActivity
import com.example.base.BR

class CreateEventActivity : CCActivity<CreateEventScreenViewModel, ActivityCreateEventBinding>() {
    override val vmClass: Class<CreateEventScreenViewModel> = CreateEventScreenViewModel::class.java
    override val layoutId: Int = R.layout.activity_create_event
    override val brRes: Int = BR.vm


    companion object {
        fun getIntentFor(context: Context) = Intent(context, CreateEventActivity::class.java)
    }
}
