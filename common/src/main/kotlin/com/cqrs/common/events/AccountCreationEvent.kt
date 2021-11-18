package com.cqrs.common.events

data class AccountCreationEvent (
    val holderId: String,
    val accountId: String
)
