package com.cqrs.command.commands

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class AccountCreationCommand(
    @TargetAggregateIdentifier
    val holderId: String,
    val accountId: String
)
