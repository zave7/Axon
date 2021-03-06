package com.cqrs.jeju.aggregate

import com.cqrs.common.command.transfer.JejuBankCancelTransferCommand
import com.cqrs.common.command.transfer.JejuBankCompensationCancelCommand
import com.cqrs.common.command.transfer.JejuBankTransferCommand
import com.cqrs.common.events.transfer.CompletedCancelTransferEvent
import com.cqrs.common.events.transfer.CompletedCompensationCancelEvent
import com.cqrs.common.events.transfer.TransferApprovedEvent
import com.cqrs.common.events.transfer.TransferDeniedEvent
import com.cqrs.jeju.command.AccountCreationCommand
import com.cqrs.jeju.event.AccountCreationEvent
import java.util.concurrent.TimeUnit
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import kotlin.random.Random
import mu.KotlinLogging
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
@Entity
@Table(name = "JEJU_ACCOUNT")
class AccountEntity {

    companion object {
        private val log = KotlinLogging.logger {}
        private val random = Random(1)
    }

    @AggregateIdentifier
    @Id
    @Column(name = "accountId")
    var accountId: String = ""
    @Column(name = "balance")
    var balance: Long = 0

    constructor()

    @CommandHandler
    constructor(command: AccountCreationCommand) {
        log.debug { "event $command" }
        if(command.balance <= 0)
            throw IllegalAccessException("유효하지 않은 입력입니다.")
        this.accountId = command.accountId
        AggregateLifecycle.apply(
            AccountCreationEvent(
                accountId = command.accountId
                , balance = command.balance
            )
        )
    }

    @EventSourcingHandler
    fun on(event: AccountCreationEvent) {
        log.debug { "event $event" }
        this.accountId = event.accountId
        this.balance = event.balance
    }

    @CommandHandler
    protected fun on(command: JejuBankTransferCommand) {
        if (random.nextBoolean())
            TimeUnit.SECONDS.sleep(10);

        log.debug { "handling $command" }
        this.accountId = command.srcAccountId
        // 잔고가 부족할 결우
        if(this.balance < command.amount) {
            AggregateLifecycle.apply(
                TransferDeniedEvent(
                    srcAccountId = command.srcAccountId
                    , dstAccountId = command.dstAccountId
                    , amount = command.amount
                    , description = "잔고가 부족합니다."
                    , transferId = command.transferId
                )
            )
        } else {
            AggregateLifecycle.apply(
                TransferApprovedEvent(
                    srcAccountId = command.srcAccountId
                    , dstAccountId = command.dstAccountId
                    , amount = command.amount
                    , transferId = command.transferId
                )
            )
        }
    }

    @EventSourcingHandler
    protected fun on(event: TransferApprovedEvent) {
        log.debug { "event $event" }
        this.balance -= event.amount
    }

    @CommandHandler
    protected fun on(command: JejuBankCancelTransferCommand) {
        log.debug { "handling $command" }
        AggregateLifecycle.apply(
            CompletedCancelTransferEvent(
                srcAccountId = command.srcAccountId
                , dstAccountId = command.dstAccountId
                , transferId = command.transferId
                , amount = command.amount
            )
        )
    }

    @EventSourcingHandler
    protected fun on(event: CompletedCancelTransferEvent) {
        log.debug { "event $event" }
        this.balance += event.amount
    }

    @CommandHandler
    protected fun on(command: JejuBankCompensationCancelCommand) {
        AggregateLifecycle.apply(
            CompletedCompensationCancelEvent(
                srcAccountId = command.srcAccountId
                , dstAccountId = command.dstAccountId
                , transferId = command.transferId
                , amount = command.amount
            )
        )
    }

    @EventSourcingHandler
    protected fun on(event: CompletedCompensationCancelEvent) {
        log.debug { "event $event" }
        this.balance -= event.amount
    }
}