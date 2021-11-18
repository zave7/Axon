package com.cqrs.command.dto

open class TransactionDTO(
    open val accountId: String,
    open val holderId: String,
    open val amount: Long
)
