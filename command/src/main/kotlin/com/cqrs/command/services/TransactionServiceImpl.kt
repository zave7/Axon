package com.cqrs.command.services

import com.cqrs.command.commands.AccountCreationCommand
import com.cqrs.command.commands.DepositMoneyCommand
import com.cqrs.command.commands.HolderCreationCommand
import com.cqrs.command.commands.MoneyTransferCommand
import com.cqrs.command.commands.WithdrawMoneyCommand
import com.cqrs.command.dto.AccountDTO
import com.cqrs.command.dto.DepositDTO
import com.cqrs.command.dto.HolderDTO
import com.cqrs.command.dto.TransferDTO
import com.cqrs.command.dto.WithdrawalDTO
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.math.log
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl(private val commandGateway: CommandGateway) : TransactionService {

    companion object {
        private val log = KotlinLogging.logger {}
    }

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

    override fun transferMoney(transferDTO: TransferDTO): String {
        log.debug { "transferMoney transferDTO : $transferDTO" }
        val moneyTransferCommand = MoneyTransferCommand.of(transferDTO)
        log.debug { "moneyTransferCommand : $moneyTransferCommand" }
        val result = commandGateway.sendAndWait<String>(moneyTransferCommand)
        log.debug { "transferMoney result : $result" }
        return result ?: "empty" // sendAndWait 에서 null 이 반환되는 원인을 파악하지 못함
    }
}