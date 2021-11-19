package com.cqrs.jeju.service

import com.cqrs.jeju.command.AccountCreationCommand
import com.cqrs.jeju.dto.AccountDTO
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(private val commandGateway: CommandGateway) : AccountService {

    override fun createAccount(accountDTO: AccountDTO) : String {
        return commandGateway.sendAndWait(
            AccountCreationCommand(
                accountId = accountDTO.accountId
                , balance = accountDTO.balance
            )
        )
    }

}