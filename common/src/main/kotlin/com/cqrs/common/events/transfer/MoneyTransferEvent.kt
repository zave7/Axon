package com.cqrs.common.events.transfer

import com.cqrs.common.command.transfer.factory.TransferCommandFactory

/**
 * Command 모듈로 계좌이체 요청이 들어오면, 인출 대상 계좌번호(srcAccountId) 및 입금 계좌번호(dstAccountId),
 * 그리고 이체 금액(amount)와 같은 기본정보와 트랜잭션간 고유 키(transferId) 및 요청 Command 구분 정보를 담고 있다.
 */
data class MoneyTransferEvent(
    val dstAccountId: String,
    val srcAccountId: String,
    val amount: Long,
    val transferId: String,
    val commandFactory: TransferCommandFactory
)