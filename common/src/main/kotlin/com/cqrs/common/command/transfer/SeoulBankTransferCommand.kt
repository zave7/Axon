package com.cqrs.common.command.transfer

class SeoulBankTransferCommand : AbstractTransferCommand() {

    override fun toString(): String {
        return """
            SeoulBankTransferCommand {
            srcAccountId = $srcAccountId \n
            dstAccountId = $dstAccountId \n
            amount = $amount \n
            transferId = $transferId }
        """
    }
}