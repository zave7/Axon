package com.cqrs.jeju.component

import com.cqrs.common.query.loan.LoanLimitQuery
import com.cqrs.common.query.loan.LoanLimitResult
import mu.KotlinLogging
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class AccountLoanComponent {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    @QueryHandler
    fun on(query: LoanLimitQuery) : LoanLimitResult {
        log.debug { "handling $query" }
        return LoanLimitResult(
            holderId = query.holderId
            , balance = query.balance
            , bankName = "JejuBank"
            , loanLimit = (query.balance * 1.2).toLong()
        )
    }

}