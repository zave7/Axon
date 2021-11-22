package com.cqrs.common.command.transfer

class SeoulBankCompensationCancelCommand : AbstractCompensationCancelCommand() {

    override fun toString(): String {
        return """
            SeoulBankCancelTransferCommand {
                srcAccountID= $srcAccountId
                , dstAccountID= $dstAccountId
                , amount= $amount
                , transferID= $transferId
                }
        """.trimIndent()
    }
}