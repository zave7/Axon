package com.cqrs.common.events

import org.axonframework.serialization.Revision

@Revision("1.0")
// 이벤트에 변경사항이 발생했음을 알리는 마커가 필요
// EventStore 에는 해당 Event 의 버전이 저장되며
// 추후 Event Upcasting 시에 해당 정보가 사용된다.
data class HolderCreationEvent (
    val holderId: String,
    val holderName: String,
    val tel: String,
    val address: String,
    val company: String
)