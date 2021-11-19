package com.cqrs.common.command.transfer

class JejuBankTransferCommand : AbstractTransferCommand() {

    override fun toString(): String {
        return """
            JejuBankTransferCommand {
            srcAccountId = $srcAccountId \n
            dstAccountId = $dstAccountId \n
            amount = $amount \n
            transferId = $transferId }
        """
    }
}