package com.cqrs.query.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "MV_ACCOUNT")
class HolderAccountSummaryEntity (

    @Id
    @Column(name = "holder_id", nullable = false)
    val holderId: String = "",
    @Column(name = "name", nullable = false)
    val name: String = "",
    @Column(name = "tel")
    val tel: String = "",
    @Column(name = "address", nullable = false)
    val address: String = "",
    @Column(name = "total_balance", nullable = false)
    var totalBalance: Long = 0,
    @Column(name = "account_cnt", nullable = false)
    var accountCnt: Long = 0
)