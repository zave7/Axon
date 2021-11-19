package com.cqrs.common.events.transfer

/**
 * 계좌 이체가 거절될 때, 요청 App에 거절 내역을 전달하기 위한 Event
 */
data class TransferDeniedEvent(
    val srcAccountId: String,
    val dstAccountId: String,
    val transferId: String,
    val amount: Long,
    val description: String
)