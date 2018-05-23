package com.ekalips.cahscrowd.data.statistics

import com.ekalips.cahscrowd.data.user.model.BaseUser
import com.ekalips.cahscrowd.data.user.remote.model.RemoteBaseUser
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteStatisticData(@Json(name = "totalAmount") override var totalAmount: Double,
                               @Json(name = "totalTurnover") override var totalTurnover: Double,
                               @Json(name = "actions") override var actionsCount: Int,
                               @Json(name = "participants") override var participants: Int,
                               @Transient override var debts: List<DebtData> = emptyList()) : StatisticData {
    @Json(name = "debts")
    var remoteDebts: List<RemoteDebtData> = emptyList()
        set(value) {
            debts = value
        }
}

@JsonClass(generateAdapter = true)
data class RemoteDebtData(@Json(name = "amount") override var amount: Double,
                          @Transient override var user: BaseUser? = null) : DebtData {
    @Json(name = "user")
    var remoteUser: RemoteBaseUser? = null
        set(value) {
            value?.let { user = it }
        }
}