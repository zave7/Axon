package com.cqrs.command.config

import com.cqrs.command.aggreate.AccountAggregate
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.common.caching.Cache
import org.axonframework.common.caching.WeakReferenceCache
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventsourcing.AggregateFactory
import org.axonframework.eventsourcing.AggregateSnapshotter
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventsourcing.GenericAggregateFactory
import org.axonframework.eventsourcing.SnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.modelling.command.Repository
import org.axonframework.springboot.autoconfig.AxonAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * AxonFramework는 기본적으로 AxonAutoConfiguration 클래스를 통해 Default 속성을 정의한다.
 * Custom 속성을 추가하기 위해 Axon 기본 설정이 완료된 후 수행될 수 있도록 @AutoConfigureAfter 어노테이션을 추가한다.
 */
@Configuration
@AutoConfigureAfter(AxonAutoConfiguration::class)
class AxonConfig {

    /* AxonServer 와 연결시 기본적으로 CommandBus로써 AxonServerCommandBus를 사용한다.
     * 이를 개선하기 위해서 Command 처리시 AxonServer 연결없이 명령을 처리하도록 변경이 필요하다.
     * AxonFramework에서는 SimpleCommandBus 클래스를 제공하며, 설정을 통해 CommandBus 인터페이스 교체가 가능하다.
     *
     * 하지만 다른 모듈에 대하여 Command 요청이 필요할 경우 SimpleCommandBus 설정을 해제해야 한다.
     */
    /*@Bean
    fun commandBus(transactionManager: TransactionManager) : SimpleCommandBus {
        return SimpleCommandBus.builder()
            .transactionManager(transactionManager)
            .build()
    }*/

    /** 이하 설정은 성능 개선을 위해 일정 주기별로 Aggregate에 대한 Snapshot 을 생성한다 **/
    /**
     * 여기서 snapshot은 특정 시점의 Aggregate의 상태를 말한다
     * Aggregate 의 발행된 Event가 5개 이상일 경우 Snapshot 을 생성하도록 지정 (비즈니스 로직에 따라 조절)
     */
    @Bean
    fun aggregateFactory() : AggregateFactory<AccountAggregate> {
        return GenericAggregateFactory(AccountAggregate::class.java)
    }

    @Bean
    fun snapshotter(eventStore: EventStore, transactionManager: TransactionManager) : Snapshotter{
        return AggregateSnapshotter.builder()
            .eventStore(eventStore)
            .aggregateFactories(aggregateFactory())
            .transactionManager(transactionManager)
            .build()
    }

    @Bean
    fun snapshotTriggerDefinition(eventStore: EventStore, transactionManager: TransactionManager) : SnapshotTriggerDefinition {
        val SNAPSHOT_TRHREHOLD = 5
        return EventCountSnapshotTriggerDefinition(snapshotter(eventStore, transactionManager), SNAPSHOT_TRHREHOLD)
    }

    @Bean
    fun accountAggregateRepository(eventStore: EventStore, snapshotTriggerDefinition: SnapshotTriggerDefinition) : Repository<AccountAggregate> {
        return EventSourcingRepository.builder(AccountAggregate::class.java)
            .eventStore(eventStore)
            .snapshotTriggerDefinition(snapshotTriggerDefinition)
            .build()
    }

    /**
     * Aggregate를 매번 로딩하면 이를 복원하는데 드는 비용이 지속 수반되므로 자주 사용하는 Aggregate의 경우 Cache를 적용
     */
    @Bean
    fun cache() : Cache {
        return WeakReferenceCache()
    }

}