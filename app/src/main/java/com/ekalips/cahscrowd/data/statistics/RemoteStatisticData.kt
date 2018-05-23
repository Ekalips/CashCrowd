package com.ekalips.cahscrowd.data.statistics

import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.data.user.remote.model.RemoteBaseUser
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteStatisticData(override var totalAmount: Double,
                               override var totalTurnover: Double,
                               override var actionsCount: Int,
                               override var participants: Int,
                               @Transient override var debts: List<DebtData> = emptyList()) : StatisticData {
    var remoteDebts: List<RemoteDebtData> = emptyList()
        set(value) {
            debts = value
        }
}

@JsonClass(generateAdapter = true)
data class RemoteDebtData(override var amount: Double,
                          @Transient override var user: BaseUser? = null) : DebtData {
    var remoteUser: RemoteBaseUser? = null
        set(value) {
            value?.let { user = it }
        }
}