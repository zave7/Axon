package com.cqrs.query.controller

import com.cqrs.query.entities.HolderAccountSummaryEntity
import com.cqrs.query.service.QueryService
import javax.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class HolderAccountController(private val queryService: QueryService) {

    @PostMapping("/reset")
    fun reset() {
        queryService.reset()
    }

    // point to point
    @GetMapping("/account/info/{id}")
    fun getAccountInfo(@PathVariable(value = "id") @NonNull @NotBlank holderId: String) : ResponseEntity<HolderAccountSummaryEntity> {
        return ResponseEntity.ok()
            .body(queryService.getAccountInfo(holderId))
    }

    // subscription
    // SSE 방식 (Server Sent Event)
    @GetMapping("/account/info/subscription/{id}")
    fun getAccountInfoSubscription(@PathVariable(value = "id") @NonNull @NotBlank holderId: String) : ResponseEntity<Flux<HolderAccountSummaryEntity>> {
        return ResponseEntity.ok()
            .body(queryService.getAccountInfoSubscription(holderId))
    }
}