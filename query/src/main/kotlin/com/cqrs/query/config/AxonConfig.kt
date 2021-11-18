package com.cqrs.query.config

import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration
import org.axonframework.eventhandling.async.SequentialPerAggregatePolicy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig {

    @Autowired
    fun configure(configurer: EventProcessingConfigurer) {
        /*  kotlin 스타일 가이드에서 람다를 소괄호에서 빼내서 함수를 콜하는 부분 뒤에
            중괄호를 열고 람다를 사용할 수 있는 경우는
            그 함수의 파라미터 마지막에 Function 이 오는 경우다.

            따라서 함수의 파라미터가 단 하나의 람다만을 가질 경우에는 소괄호를
            생략하고 함수 콜() 뒤에 람다를 사용할 수 있다.
         */
        // Replay Batch Size 를 기본값 1에서 100으로 늘림
        configurer.registerTrackingEventProcessor(
            "accounts",
            org.axonframework.config.Configuration::eventStore
        ) { TrackingEventProcessorConfiguration.forParallelProcessing(3) // 병렬도 설정 (3개의 스레드)
                .andBatchSize(1)
        }

        configurer.registerSequencingPolicy("accounts")
        { SequentialPerAggregatePolicy.instance() }
    }
}