package com.ekalips.cahscrowd.stuff.utils

import android.content.Context
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.action.Action

object EventUtils {

    @JvmStatic
    fun getEventActionInfo(context: Context, action: Action?): String {
        return action?.let {
            val userName = getUserName(context, action)
            return when {
                it.amount > 0 -> context.getString(R.string.action_info_income, userName, it.amount.toString())
                it.amount < 0 -> context.getString(R.string.action_info_spent, userName, it.amount.toString())
                else -> ""
            }
        } ?: ""
    }

    @JvmStatic
    fun getUserName(context: Context, action: Action?): String {
        return if (action?.user?.name.isNullOrBlank()) context.getString(R.string.participant) else action?.user?.name!!
    }

}