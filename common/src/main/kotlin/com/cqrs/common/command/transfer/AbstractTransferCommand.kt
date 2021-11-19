package com.cqrs.common.command.transfer

import org.axonframework.modelling.command.TargetAggregateIdentifier

abstract class AbstractTransferCommand {

    @TargetAggregateIdentifier
    lateinit var srcAccountId: String
    lateinit var dstAccountId: String
    var amount: Long = 0
    lateinit var transferId: String

    fun create(srcAccountId: String, dstAccountId: String, amount: Long, transferId: String) : AbstractTransferCommand {
        this.srcAccountId = srcAccountId
        this.dstAccountId = dstAccountId
        this.amount = amount
        this.transferId = transferId
        return this
    }
}