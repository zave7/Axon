package com.cqrs.command.commands

import org.axonframework.modelling.command.TargetAggregateIdentifier

/**
 * 계좌이체 요청 및 반영을 위한 Command 요청을 위한 커맨드 클래스
 */
data class TransferApprovedCommand(
    @TargetAggregateIdentifier
    val accountId: String,
    val amount: Long,
    val transferId: String
)