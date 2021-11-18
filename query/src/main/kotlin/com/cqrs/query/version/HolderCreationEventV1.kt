package com.cqrs.query.version

import com.cqrs.common.events.HolderCreationEvent
import org.axonframework.serialization.SimpleSerializedType
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation
import org.axonframework.serialization.upcasting.event.SingleEventUpcaster
import org.dom4j.Document

/**
 * 해당 클래스는 변경 이전 HolderCreation Event에 대해서 변경 후에
 * 어떻게 추가된 정보를 어떻게 처리할 것인지에 대한 지정 용도로 사용된다.
 */
class HolderCreationEventV1 : SingleEventUpcaster() {

    private val targetType =  SimpleSerializedType(HolderCreationEvent::class.java.typeName, null)
    // 최초에는 @Revision 정보를 명시하지 않았으므로 존재하는 값이 없기 때문에 null 로 지정하였다
    // 만약 존재하면 해당 Revision 정보를 지정해야한다.

    override fun canUpcast(intermediateRepresentation: IntermediateEventRepresentation): Boolean {
        return intermediateRepresentation.type?.equals(targetType) ?: false
    }

    override fun doUpcast(intermediateRepresentation: IntermediateEventRepresentation): IntermediateEventRepresentation {
        return intermediateRepresentation.upcastPayload(
            SimpleSerializedType(targetType.name, "1.0"),
            Document::class.java
        ) { it.apply { rootElement.addElement("company").text = "N/A" } }
    }
}