package com.cqrs.command.dto

import com.cqrs.command.commands.MoneyTransferCommand

data class TransferDTO(
    val srcAccountId: String,
    val dstAccountId: String,
    val amount: Long,
    val bankType: MoneyTransferCommand.BankType
)