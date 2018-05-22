package com.ekalips.cahscrowd.providers

import android.app.Activity
import android.support.v4.app.ShareCompat
import javax.inject.Inject

class ShareProvider @Inject constructor() {

    fun shareText(activity: Activity, text: String) {
        ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(text)
                .startChooser()
    }

}