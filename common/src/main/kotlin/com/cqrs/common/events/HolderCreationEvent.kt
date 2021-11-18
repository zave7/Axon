package com.cqrs.common.events

data class HolderCreationEvent (
    val holderId: String,
    val holderName: String,
    val tel: String,
    val address: String
)