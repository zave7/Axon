package com.cqrs.common.command.transfer

/**
 * timeout 발생으로 인한 보상트랜잭션 커맨드 클래스
 */
abstract class AbstractCompensationCancelCommand {
    lateinit var srcAccountId: String
    lateinit var dstAccountId: String
    var amount: Long = 0
    lateinit var transferId: String

    fun create(srcAccountId: String, dstAccountId: String, amount: Long, transferId: String) : AbstractCompensationCancelCommand {
        this.srcAccountId = srcAccountId
        this.dstAccountId = dstAccountId
        this.transferId = transferId
        this.amount = amount
        return this
    }
}