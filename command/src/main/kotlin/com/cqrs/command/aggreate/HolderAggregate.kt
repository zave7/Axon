package com.cqrs.command.aggreate

import com.cqrs.command.commands.HolderCreationCommand
import com.cqrs.common.events.HolderCreationEvent
import mu.KotlinLogging
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class HolderAggregate() {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    @AggregateIdentifier
    var holderId: String = ""
    var holderName: String = ""
    var tel: String = ""
    var address: String = ""

    @CommandHandler
    // 계정 생성의 명령은 곧 HolderAggregate 의 생성을 의미하는 것이다.
    constructor(command: HolderCreationCommand) : this() {
        log.debug { "log handling $command"}
        // AggregateIdentifier 에 id 값 저장은 CommandHandler에서 해야한다.
        // EventSourcingHandler 에서는 적용되지 않는다...
        this.holderId = command.holderId
        // 해당 apply 메서드를 통해 이벤트를 발행한다.
        AggregateLifecycle.apply(
            HolderCreationEvent(
                holderId = command.holderId,
                holderName = command.holderName,
                tel = command.tel,
                address = command.address
            )
        )
    }

    @EventSourcingHandler
    // 이벤트소싱 핸들러는 커맨드 핸들러에서 기존에 발행된 이벤트 및 현재 발생한 커맨드 이벤트를 적용하는 역할을 수행한다.
    protected fun createAccount(event: HolderCreationEvent) {
        log.debug { "log applying $event" }
        this.holderId = event.holderId
        this.holderName = event.holderName
        this.tel = event.tel
        this.address = event.address
        log.debug { "log HolderAggregate ${this.holderId}" }
    }
}