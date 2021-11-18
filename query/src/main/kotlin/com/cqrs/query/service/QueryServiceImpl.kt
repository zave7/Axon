package com.cqrs.query.service

import com.cqrs.query.entities.HolderAccountSummaryEntity
import com.cqrs.query.query.AccountQuery
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
    private val queryGateway: QueryGateway
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

}