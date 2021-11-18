package com.cqrs.command.services

import com.cqrs.command.commands.AccountCreationCommand
import com.cqrs.command.commands.DepositMoneyCommand
import com.cqrs.command.commands.HolderCreationCommand
import com.cqrs.command.commands.WithdrawMoneyCommand
import com.cqrs.command.dto.AccountDTO
import com.cqrs.command.dto.DepositDTO
import com.cqrs.command.dto.HolderDTO
import com.cqrs.command.dto.WithdrawalDTO
import java.util.UUID
import java.util.concurrent.CompletableFuture
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl(private val commandGateway: CommandGateway) : TransactionService {

    override fun createHolder(holderDTO: HolderDTO): CompletableFuture<String> {
        return commandGateway.send(
            HolderCreationCommand(
                holderId = UUID.randomUUID().toString()
                , holderName = holderDTO.holderName
                , tel = holderDTO.tel
                , address = holderDTO.address
                , company = holderDTO.company
            )
        )
    }

    override fun createAccount(accountDTO: AccountDTO): CompletableFuture<String> {
        return commandGateway.send(
            AccountCreationCommand(
                holderId = accountDTO.holderId
                , accountId = UUID.randomUUID().toString()
            )
        )
    }

    override fun depositMoney(transactionDTO: DepositDTO): CompletableFuture<String> {
        return commandGateway.send(
            DepositMoneyCommand(
                accountId = transactionDTO.accountId
                , holderId = transactionDTO.holderId
                , amount = transactionDTO.amount
            )
        )
    }

    override fun withdrawMoney(transactionDTO: WithdrawalDTO): CompletableFuture<String> {
        return commandGateway.send(
            WithdrawMoneyCommand(
                accountId = transactionDTO.accountId
                , holderId = transactionDTO.holderId
                , amount = transactionDTO.amount
            )
        )
    }
}