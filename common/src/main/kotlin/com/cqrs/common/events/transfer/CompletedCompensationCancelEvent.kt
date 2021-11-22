package com.cqrs.common.events.transfer

data class CompletedCompensationCancelEvent(
    val srcAccountId: String,
    val dstAccountId: String,
    val amount: Long,
    val transferId: String
)