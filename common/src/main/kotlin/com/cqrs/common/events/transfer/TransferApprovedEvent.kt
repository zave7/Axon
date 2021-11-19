package com.cqrs.common.events.transfer

/**
 * 계좌 이체가 성공되었을 때, 금액을 반영하기 위한 Event
 */
data class TransferApprovedEvent(
    val srcAccountId: String,
    val dstAccountId: String,
    val transferId: String,
    val amount: Long
)