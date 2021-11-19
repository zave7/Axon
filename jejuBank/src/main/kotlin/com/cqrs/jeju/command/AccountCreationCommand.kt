package com.cqrs.jeju.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class AccountCreationCommand(
    @TargetAggregateIdentifier
    val accountId: String,
    val balance: Long
)