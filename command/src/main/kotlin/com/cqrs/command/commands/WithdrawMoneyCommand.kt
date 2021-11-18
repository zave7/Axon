package com.cqrs.command.commands

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class WithdrawMoneyCommand(
    @TargetAggregateIdentifier
    val accountId: String,
    val holderId: String,
    val amount: Long
)
