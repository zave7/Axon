package com.cqrs.common.query.loan

data class LoanLimitResult(
    val holderId: String,
    val bankName: String,
    val balance: Long,
    val loanLimit: Long
)
