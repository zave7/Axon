package com.cqrs.command.saga

import com.cqrs.command.commands.TransferApprovedCommand
import com.cqrs.command.event.DepositCompletedEvent
import com.cqrs.common.command.transfer.AbstractTransferCommand
import com.cqrs.common.command.transfer.factory.TransferCommandFactory
import com.cqrs.common.events.transfer.MoneyTransferEvent
import com.cqrs.common.events.transfer.TransferApprovedEvent
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

@Saga
class TransferManager {

    @Autowired
    @Transient
    private lateinit var commandGateway: CommandGateway
    private lateinit var commandFactory: TransferCommandFactory

    companion object {
        private val log = KotlinLogging.logger {}
    }

    init {
        log.debug { "Created saga instance Default Constructor " }
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "transferId")
    // associationProperty 는 해당 인스턴스를 유일하게 구별할 수 있는 속성을 지정
    protected fun on(event: MoneyTransferEvent) {
        log.debug { "Created saga instance" }
        log.debug { "event : $event" }
        this.commandFactory = event.commandFactory
        SagaLifecycle.associateWith("srcAccountId", event.srcAccountId)
        log.info { "계좌 이체 시작 : $event" }
        commandGateway.send<AbstractTransferCommand>(commandFactory.getTransferCommand())
    }

    @SagaEventHandler(associationProperty = "srcAccountId")
    protected fun on(event: TransferApprovedEvent) {
        log.info { "이체 금액 ${event.amount} 계좌 반영 요청 : $event" }
        SagaLifecycle.associateWith("accountId", event.dstAccountId)
        commandGateway.send<TransferApprovedCommand>(
            TransferApprovedCommand(
                accountId = event.dstAccountId
                , amount = event.amount
                , transferId = event.transferId
            )
        )
    }

    @SagaEventHandler(associationProperty = "accountId")
    @EndSaga
    // 트랙잭션이 끝나면 종료 DepositCompletedEvent 메세지를 수신받으면 Saga 인스턴스 종료
    // Saga 인스턴스 종료방법은 크게 두가지이다 @EndSaga 어노테이션을 지정하거나 명시적으로 end 메서드를 기입하거나
    protected fun on(event: DepositCompletedEvent) {
        log.info { "계좌 이체 성공 : $event" }
    }

}