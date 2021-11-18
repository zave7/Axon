package com.cqrs.command.commands

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class HolderCreationCommand(
    @TargetAggregateIdentifier
    var holderId: String,
    var holderName: String,
    var tel: String,
    var address: String
)