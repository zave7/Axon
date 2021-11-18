package com.cqrs.query.service

import com.cqrs.common.query.loan.LoanLimitQuery
import com.cqrs.common.query.loan.LoanLimitResult
import com.cqrs.query.entities.HolderAccountSummaryEntity
import com.cqrs.query.query.AccountQuery
import com.cqrs.query.repositories.AccountRepository
import java.util.Collections
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import kotlin.streams.toList
import mu.KotlinLogging
import org.axonframework.config.Configuration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.SubscriptionQueryResult
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class QueryServiceImpl(
    private val configuration: Configuration,
    private val queryGateway: QueryGateway,
    private val repository: AccountRepository
) : QueryService{

    companion object {
        private val log = KotlinLogging.logger {}
    }

    override fun reset() {
        configuration.eventProcessingConfiguration()
            .eventProcessorByProcessingGroup("accounts",
            TrackingEventProcessor::class.java)
            .ifPresent {
                it.shutDown()
                it.resetTokens() // 실체 token 초기화
                it.start()
            }
    }

    override fun getAccountInfo(holderId: String): HolderAccountSummaryEntity? {

        val accountQuery = AccountQuery(holderId)
        log.debug { "handling $accountQuery" }
        return queryGateway.query(accountQuery, ResponseTypes.instanceOf(HolderAccountSummaryEntity::class.java)).join()
    }

    override fun getAccountInfoSubscription(holderId: String): Flux<HolderAccountSummaryEntity> {

        val accountQuery = AccountQuery(holderId)
        log.debug { "handling $accountQuery" }

        val queryResult: SubscriptionQueryResult<HolderAccountSummaryEntity, HolderAccountSummaryEntity> = queryGateway.subscriptionQuery(
            accountQuery,
            ResponseTypes.instanceOf(HolderAccountSummaryEntity::class.java),
            ResponseTypes.instanceOf(HolderAccountSummaryEntity::class.java)
        )

        // 최초에 initResult 생성 후 지속적으로 updates 메서드를 통해 Stream 데이터를 전달받아 Client 에게 전달한다.
        // 만약 중간에 Connection 이 실패하게되면, 해당 Flux 를 종료하도록 함
        return Flux.create { emitter ->
            queryResult.initialResult().subscribe(emitter::next)
            queryResult.updates()
                .doOnNext { holder ->
                    log.debug { "doOnNext : $holder isCanceled ${emitter.isCancelled}" }
                    if(emitter.isCancelled)
                        queryResult.close()
                }
                .doOnComplete(emitter::complete)
                .subscribe(emitter::next)
        }
    }

    override fun getAccountInfoScatterGather(holderId: String): List<LoanLimitResult> {
        val accountSummaryEntity = repository.findByHolderId(holderId) ?: throw NoSuchElementException("소유주가 없습니다.")

        // Scatter-Gather 쿼리는 단일 App 에 요청하는 것이 아니므로, 만약 Handler 처리 App 에 장애가 발생한다면
        // 무한정 대기할 수 있으므로 DeadLine 을 정하여 요청시간 만큼만 대기
        return queryGateway.scatterGather(
            LoanLimitQuery(
                holderId = accountSummaryEntity.holderId
                , balance = accountSummaryEntity.totalBalance
            )
            , ResponseTypes.instanceOf(LoanLimitResult::class.java)
            , 30, TimeUnit.SECONDS)
            .toList()
    }

}