package com.cqrs.common.command.transfer

/**
 * 계좌 이체 거절 커맨드 클래스
 */
abstract class AbstractCancelTransferCommand {
    lateinit var srcAccountId: String
    lateinit var dstAccountId: String
    var amount: Long = 0
    lateinit var transferId: String

    fun create(srcAccountId: String, dstAccountId: String, amount: Long, transferId: String) : AbstractCancelTransferCommand {
        this.srcAccountId = srcAccountId
        this.dstAccountId = dstAccountId
        this.transferId = transferId
        this.amount = amount
        return this
    }
    
}