package com.cqrs.jeju.event

data class AccountCreationEvent(
    val accountId: String,
    val balance: Long
)