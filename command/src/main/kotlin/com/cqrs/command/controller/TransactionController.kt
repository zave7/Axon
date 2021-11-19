package com.cqrs.command.controller

import com.cqrs.command.dto.AccountDTO
import com.cqrs.command.dto.DepositDTO
import com.cqrs.command.dto.HolderDTO
import com.cqrs.command.dto.TransferDTO
import com.cqrs.command.dto.WithdrawalDTO
import com.cqrs.command.services.TransactionService
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TransactionController(private val transactionService: TransactionService) {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    @PostMapping("/holder")
    fun createHolder(@RequestBody holderDTO: HolderDTO) : CompletableFuture<String> {
        return transactionService.createHolder(holderDTO)
    }

    @PostMapping("/account")
    fun createAccount(@RequestBody accountDTO: AccountDTO) : CompletableFuture<String> {
        return transactionService.createAccount(accountDTO)
    }

    @PostMapping("/deposit")
    fun deposit(@RequestBody transactionDTO: DepositDTO) : CompletableFuture<String> {
        return transactionService.depositMoney(transactionDTO)
    }

    @PostMapping("/withdrawal")
    fun withdraw(@RequestBody transactionDTO: WithdrawalDTO) : CompletableFuture<String> {
        return transactionService.withdrawMoney(transactionDTO)
    }

    @PostMapping("/transfer")
    fun transfer(@RequestBody transferDTO: TransferDTO) : ResponseEntity<String> {
        log.debug { "transferDTO : $transferDTO" }
        return ResponseEntity.ok()
            .body(transactionService.transferMoney(transferDTO))
    }
}