package com.cqrs.common.command.transfer.factory

import com.cqrs.common.command.transfer.AbstractCancelTransferCommand
import com.cqrs.common.command.transfer.AbstractCompensationCancelCommand
import com.cqrs.common.command.transfer.AbstractTransferCommand

class TransferCommandFactory(
    val transferCommand: AbstractTransferCommand
    , val abortTransferCommand: AbstractCancelTransferCommand
    , val compensationAbortCommand: AbstractCompensationCancelCommand
) {

    fun create(srcAccountId: String, dstAccountId: String, amount: Long, transferId: String) {
        transferCommand.create(srcAccountId, dstAccountId, amount, transferId)
        abortTransferCommand.create(srcAccountId, dstAccountId, amount, transferId)
        compensationAbortCommand.create(srcAccountId, dstAccountId, amount, transferId)
    }

}