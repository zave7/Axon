package com.cqrs.query.repositories

import com.cqrs.query.entities.HolderAccountSummaryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<HolderAccountSummaryEntity, String> {
    fun findByHolderId(holderId: String) : HolderAccountSummaryEntity?
}