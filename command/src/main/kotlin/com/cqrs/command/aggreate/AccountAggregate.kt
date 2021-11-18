package com.cqrs.command.aggreate

import com.cqrs.command.commands.AccountCreationCommand
import com.cqrs.command.commands.DepositMoneyCommand
import com.cqrs.command.commands.WithdrawMoneyCommand
import com.cqrs.common.events.AccountCreationEvent
import com.cqrs.common.events.DepositMoneyEvent
import com.cqrs.common.events.WithdrawMoneyEvent
import mu.KotlinLogging
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
// Aggregate 클래스에는 Aggregate 임을 알려주는 annotation 을 추가해준다.
// 또한 Aggregate 별로 식별자가 반드시 존재해야되기 때문에 유일성을 갖는 대표키 속성에
// @AggregateIdentifier 를 추가한다.
class AccountAggregate() {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    @AggregateIdentifier
    var accountId: String = ""
    var holderId: String = ""
    var balance: Long = 0

    @CommandHandler
    constructor(command: AccountCreationCommand) : this() {
        log.debug { "log handling $command" }
        this.accountId = command.accountId
        AggregateLifecycle.apply(
            AccountCreationEvent(
                holderId = command.holderId,
                accountId = command.accountId
            )
        )
    }

    @EventSourcingHandler
    protected fun createAccount(event: AccountCreationEvent) {
        log.debug { "log applying $event" }
        this.accountId = event.accountId
        this.holderId = event.holderId
        this.balance = 0
    }

    @CommandHandler
    // 커맨드 핸들러에서 사전 exception 처리 및 유효성 검증을 통해서 검증된 event 만을 발행
    protected fun depositMoney(command: DepositMoneyCommand) {
        log.debug { "log handling $command" }
        this.accountId = command.accountId
        if(command.amount <= 0)
            throw IllegalStateException("amount <= 0")
        AggregateLifecycle.apply(
            DepositMoneyEvent(
                holderId = command.holderId,
                accountId = command.accountId,
                amount = command.amount
            )
        )
    }

    @EventSourcingHandler
    protected fun depositMoney(event: DepositMoneyEvent) {
        log.debug { "log applying $event" }
        this.balance += event.amount
    }

    @CommandHandler
    protected fun withdrawMoney(command: WithdrawMoneyCommand) {
        log.debug { "log handling $command" }
        this.accountId = command.accountId
        if(this.balance - command.amount < 0)
            throw IllegalStateException("잔고가 부족합니다.")
        else if(command.amount <= 0)
            throw IllegalStateException("amount <= 0")
        AggregateLifecycle.apply(
            WithdrawMoneyEvent(
                holderId = command.holderId,
                accountId = command.accountId,
                amount = command.amount
            )
        )
    }

    @EventSourcingHandler
    protected fun withdrawMoney(event: WithdrawMoneyEvent) {
        log.debug { "log applying $event" }
        this.balance -= event.amount
        log.debug { "log balance ${this.balance}" }
    }
}