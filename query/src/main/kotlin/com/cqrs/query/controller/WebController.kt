package com.cqrs.query.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController {

    @GetMapping("/p2p")
    fun pointToPointQueryView() {

    }

    @GetMapping("/subscription")
    fun subscriptionQueryView() {

    }

    @GetMapping("/scatter-gather")
    fun scatterGatherQueryView() {

    }
}