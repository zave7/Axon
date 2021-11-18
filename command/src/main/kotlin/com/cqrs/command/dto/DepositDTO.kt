package com.cqrs.command.dto

data class DepositDTO(
    override val accountId: String
    , override val holderId: String
    , override val amount: Long
) : TransactionDTO(accountId, holderId, amount)
