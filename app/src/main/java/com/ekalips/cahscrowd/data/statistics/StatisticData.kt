package com.ekalips.cahscrowd.data.statistics

import com.ekalips.cahscrowd.data.user.model.BaseUser

interface StatisticData {
    var totalAmount: Double
    var totalTurnover: Double
    var actionsCount: Int
    var participants: Int
    var debts: List<DebtData>
}

interface DebtData {
    var amount: Double
    var user: BaseUser
}