package com.cqrs.common.events

data class WithdrawMoneyEvent(
    val holderId: String,
    val accountId: String,
    val amount: Long
)