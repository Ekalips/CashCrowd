package com.ekalips.cahscrowd.providers

import android.content.Context
import android.content.Intent
import com.ekalips.cahscrowd.auth.mvvm.view.AuthActivity
import com.ekalips.cahscrowd.create_event.mvvm.view.CreateEventActivity
import com.ekalips.cahscrowd.event.mvvm.view.EventActivity
import com.ekalips.cahscrowd.main.mvvm.view.MainActivity
import com.ekalips.cahscrowd.welcome.mvvm.view.SplashActivity
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Ekalips on 2/7/18.
 */


@Singleton
class GlobalNavigationProvider @Inject constructor() {
    infix fun navigateToSplashScreen(context: Context) {
        context.startActivity(Intent(context, SplashActivity::class.java))
    }

    infix fun navigateToAuthScreen(context: Context) {
        context.startActivity(Intent(context, AuthActivity::class.java))
    }

    infix fun navigateToMainScreen(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    infix fun navigateToCreateEventScreen(context: Context) {
        context.startActivity(CreateEventActivity.getIntentFor(context))
    }

    fun navigateToEventScreen(context: Context, eventId: String) {
        context.startActivity(EventActivity.getIntentFor(context, eventId))
    }
}