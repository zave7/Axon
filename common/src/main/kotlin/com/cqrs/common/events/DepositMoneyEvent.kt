package com.cqrs.common.events

data class DepositMoneyEvent(
    val holderId: String,
    val accountId: String,
    val amount: Long
)