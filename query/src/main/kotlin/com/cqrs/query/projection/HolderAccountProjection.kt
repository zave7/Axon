package com.cqrs.query.projection

import com.cqrs.common.events.AccountCreationEvent
import com.cqrs.common.events.DepositMoneyEvent
import com.cqrs.common.events.HolderCreationEvent
import com.cqrs.common.events.WithdrawMoneyEvent
import com.cqrs.query.entities.HolderAccountSummaryEntity
import com.cqrs.query.query.AccountQuery
import com.cqrs.query.repositories.AccountRepository
import java.time.Instant
import mu.KotlinLogging
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.AllowReplay
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
@EnableRetry
@ProcessingGroup("accounts")
class HolderAccountProjection {

    // @EnableRetry 적용으로인해 생성자 주입이 정상적으로 이루어 지지 않고 있어 필드 주입을 하였음
    @Autowired
    lateinit var repository: AccountRepository
    @Autowired
    lateinit var queryUpdateEmitter: QueryUpdateEmitter

    companion object {
        private val log = KotlinLogging.logger {}
    }

    @QueryHandler
    // Query Gateway 를 통한 요청 핸들링
    fun on(query: AccountQuery) : HolderAccountSummaryEntity? {
        log.debug { "handling $query" }
        return repository.findByHolderId(query.holderId)
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
    @Retryable(value = [NoSuchElementException::class], maxAttempts = 5, backoff = Backoff(delay = 1000))
    // NoSuchElementException 이 발생하면 1초 대기 후에 다시 시도하면 최대 5번 까지 재수행 시도
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

        queryUpdateEmitter.emit(
            AccountQuery::class.java
            , { query -> query.holderId == event.holderId }
            , holderAccount)

        repository.save(holderAccount)
    }

    @EventHandler
    @AllowReplay
    protected fun on(event: WithdrawMoneyEvent, @Timestamp instant: Instant) {
        log.debug { "projecting $event , timestamp : $instant" }
        val holderAccount = getHolderAccountSummary(event.holderId).apply { totalBalance -= event.amount }

        queryUpdateEmitter.emit(
            AccountQuery::class.java
            , { query -> query.holderId == event.holderId }
            , holderAccount)

        repository.save(holderAccount)
    }

    private fun getHolderAccountSummary(holderId: String) : HolderAccountSummaryEntity {
        return repository.findByHolderId(holderId)?: throw NoSuchElementException("소유주가 존재하지 않습니다.")
    }

}