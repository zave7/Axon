package com.cqrs.command.commands

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class DepositMoneyCommand(
    // AxonFramework 모델링 단위가 Aggregate 이고 각 Aggregate 마다 고유한 식별자가 부여되어야 함
    @TargetAggregateIdentifier
    val accountId: String,
    val holderId: String,
    val amount: Long
)
