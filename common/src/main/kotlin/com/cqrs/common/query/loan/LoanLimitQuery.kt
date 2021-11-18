package com.cqrs.common.query.loan

data class LoanLimitQuery(
    val holderId: String,
    val balance: Long
)