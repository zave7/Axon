package com.cqrs.common.command.transfer.factory

import com.cqrs.common.command.transfer.AbstractTransferCommand

class TransferCommandFactory(private val transferCommand: AbstractTransferCommand) {

    fun create(srcAccountId: String, dstAccountId: String, amount: Long, transferId: String) {
        transferCommand.create(srcAccountId, dstAccountId, amount, transferId)
    }

    fun getTransferCommand() : AbstractTransferCommand {
        return this.transferCommand
    }
}