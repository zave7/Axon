package com.cqrs.query.controller

import com.cqrs.query.service.QueryService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HolderAccountController(private val queryService: QueryService) {

    @PostMapping("/reset")
    fun reset() {
        queryService.reset()
    }
}