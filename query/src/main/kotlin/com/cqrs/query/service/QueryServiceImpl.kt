package com.cqrs.query.service

import org.axonframework.config.Configuration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.springframework.stereotype.Service

@Service
class QueryServiceImpl(private val configuration: Configuration) : QueryService{

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

}