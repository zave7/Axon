package com.cqrs.command.event

data class DepositCompletedEvent(
    val accountId: String,
    val transferId: String
)