package com.cqrs.common.command.transfer

class JejuBankCompensationCancelCommand : AbstractCompensationCancelCommand() {

    override fun toString(): String {
        return """
            JejuBankCancelTransferCommand {
                srcAccountID= $srcAccountId
                , dstAccountID= $dstAccountId
                , amount= $amount
                , transferID= $transferId
                }
        """.trimIndent()
    }
}