package com.cqrs.jeju.service

import com.cqrs.jeju.dto.AccountDTO

interface AccountService {
    fun createAccount(accountDTO: AccountDTO) : String
}