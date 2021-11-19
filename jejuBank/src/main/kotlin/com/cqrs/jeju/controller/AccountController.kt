package com.cqrs.jeju.controller

import com.cqrs.jeju.dto.AccountDTO
import com.cqrs.jeju.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(private val accountService: AccountService) {

    @PostMapping("/account")
    fun createAccount(@RequestBody accountDTO: AccountDTO) : ResponseEntity<String> {
        return ResponseEntity.ok()
            .body(accountService.createAccount(accountDTO))
    }
}