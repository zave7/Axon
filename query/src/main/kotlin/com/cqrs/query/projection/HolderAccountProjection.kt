package com.cqrs.query.projection

import com.cqrs.common.events.AccountCreationEvent
import com.cqrs.common.events.DepositMoneyEvent
import com.cqrs.common.events.HolderCreationEvent
import com.cqrs.common.events.WithdrawMoneyEvent
import com.cqrs.query.entities.HolderAccountSummaryEntity
import com.cqrs.query.repositories.AccountRepository
import java.time.Instant
import mu.KotlinLogging
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.AllowReplay
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("accounts")
class HolderAccountProjection(private val repository: AccountRepository) {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    @ResetHandler
    // Replay 가 수행 되기 전 초기화 작업 진행
    private fun resetHolderAccountInfo() {
        log.debug { "reset triggered" }
        repository.deleteAll()
    }

    @EventHandler
    @AllowReplay
    // Replay 적용 대상 Handler 메서드에 @AllowReplay 어노테이션을 추가해준다.
    // 만약 Replay 대상에서 해당 Event 는 처리하고 싶지 않을 경우에는 @DisallowReplay 를 추가해준다.
    protected fun on(event: HolderCreationEvent, @Timestamp instant: Instant) {
        log.debug { "projecting $event , timestamp : $instant" }
        val holderAccountSummary = HolderAccountSummaryEntity(
            holderId = event.holderId,
            name = event.holderName,
            address = event.address,
            tel = event.tel,
            totalBalance = 0,
            accountCnt = 0
        )

        repository.save(holderAccountSummary)
    }

    @EventHandler
    @AllowReplay
    // EventHandler 메서드 파라미터에는 Timestamp와 @SequenceNumber, ReplayStatus 등이 추가로 전달될 수 있다
    protected fun on(event: AccountCreationEvent, @Timestamp instant: Instant) {
        log.debug { "projecting $event , timestamp : $instant" }
        val holderAccount = getHolderAccountSummary(event.holderId).apply { accountCnt += 1 }
        repository.save(holderAccount)
    }

    @EventHandler
    @AllowReplay
    protected fun on(event: DepositMoneyEvent, @Timestamp instant: Instant) {
        log.debug { "projecting $event , timestamp : $instant" }
        val holderAccount = getHolderAccountSummary(event.holderId).apply { totalBalance += event.amount }
        repository.save(holderAccount)
    }

    @EventHandler
    @AllowReplay
    protected fun on(event: WithdrawMoneyEvent, @Timestamp instant: Instant) {
        log.debug { "projecting $event , timestamp : $instant" }
        val holderAccount = getHolderAccountSummary(event.holderId).apply { totalBalance -= event.amount }
        repository.save(holderAccount)
    }

    private fun getHolderAccountSummary(holderId: String) : HolderAccountSummaryEntity {
        return repository.findByHolderId(holderId)?: throw NoSuchElementException("소유주가 존재하지 않습니다.")
    }

}