package com.ekalips.cahscrowd.data.statistics

import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.data.user.remote.model.RemoteBaseUser

data class RemoteStatisticData(override var totalAmount: Double,
                               override var totalTurnover: Double,
                               override var actionsCount: Int,
                               override var participants: Int,
                               @Transient override var debts: List<DebtData>) : StatisticData {
    var remoteDebts: List<RemoteDebtData> = emptyList()
        set(value) {
            debts = value
        }
}

data class RemoteDebtData(override var amount: Double,
                          @Transient override var user: BaseUser) : DebtData {
    var remoteUser: RemoteBaseUser? = null
        set(value) {
            value?.let { user = it }
        }
}