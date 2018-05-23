package com.ekalips.cahscrowd.stuff.utils

import android.content.Context
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.data.action.Action
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap

object EventUtils {

    private val numberFormat = NumberFormat.getCurrencyInstance(Locale("ua_UA")).also {
        it.currency = Currency.getInstance("UAH")
    }

    private val formattedCache: MutableMap<Double, String> = HashMap()

    @JvmStatic
    fun formatCurrency(amount: Double?): String {
        return when {
            amount == null -> ""
            formattedCache.containsKey(amount) -> formattedCache[amount]!!
            else -> {
                val formatted = numberFormat.format(amount)
                formattedCache[amount] = formatted
                formatted
            }
        }
    }

    @JvmStatic
    fun getEventActionInfo(context: Context, action: Action?): String {
        return action?.let {
            val userName = getUserName(context, action)

            val amount = formatCurrency(Math.abs(it.amount))

            return when {
                it.amount > 0 -> context.getString(R.string.action_info_income, userName, amount)
                it.amount < 0 -> context.getString(R.string.action_info_spent, userName, amount)
                else -> ""
            }
        } ?: ""
    }

    @JvmStatic
    fun getUserName(context: Context, action: Action?): String {
        return if (action?.user?.name.isNullOrBlank()) context.getString(R.string.participant) else action?.user?.name!!
    }

}