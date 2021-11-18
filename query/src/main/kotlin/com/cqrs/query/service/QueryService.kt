package com.cqrs.query.service

import com.cqrs.query.entities.HolderAccountSummaryEntity
import reactor.core.publisher.Flux

interface QueryService {
    fun reset()
    fun getAccountInfo(holderId: String) : HolderAccountSummaryEntity?
    fun getAccountInfoSubscription(holderId: String) : Flux<HolderAccountSummaryEntity>
}