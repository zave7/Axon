package com.cqrs.seoul.component

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
            , bankName = "SeoulBank"
            , loanLimit = (query.balance * 1.5).toLong()
        )
    }

}