package com.cqrs.command.services

import com.cqrs.command.dto.AccountDTO
import com.cqrs.command.dto.DepositDTO
import com.cqrs.command.dto.HolderDTO
import com.cqrs.command.dto.TransferDTO
import com.cqrs.command.dto.WithdrawalDTO
import java.util.concurrent.CompletableFuture

interface TransactionService {
    fun createHolder(holderDTO: HolderDTO) : CompletableFuture<String>
    fun createAccount(accountDTO: AccountDTO) : CompletableFuture<String>
    fun depositMoney(transactionDTO: DepositDTO) : CompletableFuture<String>
    fun withdrawMoney(transactionDTO : WithdrawalDTO) : CompletableFuture<String>
    fun transferMoney(transferDTO: TransferDTO) : String
}